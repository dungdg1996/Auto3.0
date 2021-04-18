package app.process;

import app.Conts;
import app.utils.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Finder {

    private static final Finder instance = new Finder();
    private final File root;
    private final Map<String, String> result;
    private final Random random;

    private Finder() {
        this.root = new File(Conts.Path.CHU_KY_MAU);
        this.random = new Random();
        this.result = new HashMap<>();
    }

    public String findByName(String key, String soGiayTo) {
        if (result.containsKey(soGiayTo)) return result.get(soGiayTo);
        String KEY = StringUtils.removeAccent(key).toUpperCase().trim();
        FilenameFilter filter = (dir, name) -> {
            String[] list = name.split("_");
            for (String s : list)
                if (s.toUpperCase().equals(KEY)) {
                    return true;
                }
            return false;
        };
        String[] list = root.list(filter);
        if (list == null || list.length == 0) return null;
        String fileName = Conts.Path.CHU_KY_MAU + "\\" + list[random.nextInt(list.length)];
        result.put(soGiayTo, fileName);
        return fileName;
    }

    public static Finder getInstance() {
        return instance;
    }

    public boolean haveResult(String soGiayTo) {
        return result.containsKey(soGiayTo);
    }
}
