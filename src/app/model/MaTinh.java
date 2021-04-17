package app.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MaTinh {
    public static final String PROPERTY_NAME = "matinh.properties";
    public static final String PROPERTY_ID = "macmnd.properties";
    private static final Properties NAME;
    private static final Properties ID;

    static {
       NAME = getProperties(PROPERTY_NAME);
       ID = getProperties(PROPERTY_ID);
    }

    private static Properties getProperties(String proName){
        Properties properties = new Properties();
        InputStream inputStream = MaTinh.class.getClassLoader().getResourceAsStream(proName);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("File not found: " + proName);
        return properties;
    }

    public static String getName(String maTinh) {
        return (String) NAME.getOrDefault(maTinh, "Sai mã tỉnh");
    }

    public static String getId(String maTinh) {
        return (String) ID.getOrDefault(maTinh, "Sai mã tỉnh");
    }

    public static boolean exits(String maTinh) {
        return NAME.get(maTinh) != null;
    }
}
