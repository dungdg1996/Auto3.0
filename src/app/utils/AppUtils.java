package app.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppUtils {

    private static final SimpleDateFormat DD = new SimpleDateFormat("dd");
    private static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat MM = new SimpleDateFormat("MM");

    public static Map<String, String> covertToStringMap(Object obj) {
        Map<String, String> map = new HashMap<>();
        AppUtils.merge(map, obj);
        return map;
    }

    public static void merge(Map<String, String> map, Object obj) {
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    String key = "$" + pd.getName() + "$";
                    String val = String.valueOf(reader.invoke(obj));
                    map.put(key, val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSysTime(Map<String, String> map) {
        Date date = new Date();

        map.put("$ngay$", DD.format(date));

        map.put("$thang$", MM.format(date));

        map.put("$nam$", YYYY.format(date));
    }

    public static Map<String, String> clone(Map<String, String> map) {
        return new HashMap<>(map);
    }
}
