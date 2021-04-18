package app.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern pattern = Pattern.compile("(\\$\\w+\\$)");

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .replace("Đ", "D")
                .replace("đ", "d");
    }

    public static String capitalized(String s) {
        String[] arr = s.toLowerCase().split(" ");
        StringBuilder rs = new StringBuilder();
        for (String x : arr) {
            if (x.length() > 0) rs.append(x.substring(0, 1).toUpperCase());
            if (x.length() > 1) rs.append(x.substring(1));
            rs.append(" ");
        }
        return rs.toString().trim();
    }

    public static String replaceString(String s, Map<String, String> data) {
        String result = s;
        Matcher matcher = pattern.matcher(s);
        List<String> vars = new ArrayList<>();
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            vars.add(matchResult.group());
        }
        for (String var : vars) {
            result = result.replace(var, data.getOrDefault(var, var));
        }
        return result;
    }
}
