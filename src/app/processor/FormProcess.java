package app.processor;

import app.contract.ContractImage;
import app.entity.ConfigData;
import app.entity.ConfigResult;
import app.utils.StringUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormProcess {

    private static final Pattern pattern = Pattern.compile("(\\$\\w+\\$)");

    private final List<ConfigData> configs;
    private final Map<Integer, ConfigResult> results;
    private final Finder finder = Finder.getInstance();
    private final String input;
    private final int sl;
    private boolean writeSdt = true;
    private boolean writeImage = true;

    public FormProcess(List<ConfigData> configs, String input) {
        this.configs = configs;
        this.input = input;
        this.results = new HashMap<>();
        this.sl = configs.stream().map(ConfigData::getLoai)
                .filter(s -> s.startsWith("SDT_"))
                .map(s -> s.replace("SDT_", ""))
                .map(Integer::parseInt)
                .filter(integer -> integer != 0)
                .max(Integer::compare)
                .orElse(Integer.MAX_VALUE);
    }

    public int getSl() {
        return sl;
    }

    public void process(Map<String, String> data) {

        if (sl == ProcessManager.LIM && Integer.parseInt(data.get("$soLuongSdt$")) > sl) {
            writeSdt = false;
        }

        if (Integer.MAX_VALUE != sl && sl != ProcessManager.LIM && Integer.parseInt(data.get("$soLuongSdt$")) <= ProcessManager.LIM) {
            writeImage = false;
        }

        for (ConfigData config : configs) {
            String value = config.getGiaTri();
            if (config.getLoai().startsWith("SDT")) {
                String index = config.getLoai().replace("SDT_", "");
                if (!writeSdt || !index.equals(data.get("$sdtIndex$"))) {
                    continue;
                }
                value = doText(config, data);
            }
            switch (config.getLoai()) {
                case "TEXT":
                case "HINH_ANH":
                    value = doText(config, data);
                    break;
                case "CHU_KY":
                    value = doChuKy(config, data);
                    break;
                default:
                    break;
            }
            results.put(config.getIndex(), getConfigResult(config, value));
        }
    }

    private ConfigResult getConfigResult(ConfigData config, String value) {
        ConfigResult result = new ConfigResult();
        result.setValue(value);
        result.setType(config.getLoai());
        result.setAlign(config.getCanLe());
        result.setColor(getColor(config.getMau()));
        result.setFont(new Font(config.getFont(), getFontStyle(config.getStyle()), config.getSize()));
        result.setX(config.getX());
        result.setY(config.getY());
        return result;
    }

    private Color getColor(String s) {
        if (s == null || s.isBlank()) {
            return Color.BLACK;
        }
        if (s.equals("DO")) {
            return Color.RED;
        }
        if (s.equals("XANH_DUONG")) {
            return new Color(51, 0, 255);
        }
        if (s.startsWith("#")) {
            return Color.decode(s);
        }
        return Color.BLACK;
    }

    private int getFontStyle(String s) {
        if (s == null || s.isBlank()) {
            return Font.BOLD;
        }
        if (s.equals("THUONG")) {
            return Font.PLAIN;
        }
        if (s.equals("IN_DAM")) {
            return Font.BOLD;
        }

        if (s.equals("IN_NGHIENG")) {
            return Font.ITALIC;
        }
        return Font.BOLD;
    }

    private String doChuKy(ConfigData config, Map<String, String> data) {
        String key = doText(config, data);
        return finder.findByName(key, data.get("$soGiayTo$"));
    }

    private String doText(ConfigData config, Map<String, String> data) {
        String giaTri = config.getGiaTri();
        Matcher matcher = pattern.matcher(giaTri);
        List<String> vars = new ArrayList<>();
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            vars.add(matchResult.group());
        }
        for (String var : vars) {
            giaTri = giaTri.replace(var, data.getOrDefault(var, var));
        }
        switch (config.getKieuChu()) {
            case "HOA":
                return giaTri.toUpperCase();
            case "TEN_RIENG":
                return StringUtils.capitalized(giaTri);
            default:
                return giaTri;
        }
    }

    public void saveForm(String path) {
        if (!writeImage) return;
        ContractImage contractImage = new ContractImage(input);
        results.forEach((index, configResult) -> {
            contractImage.draw(configResult);
        });
        contractImage.save(path + "\\" + new File(input).getName());
    }

    public void clear() {
        this.results.clear();
    }
}
