package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.net.URL;


public class Conts {

    public final static String FXML_PREFIX = "/fxml/";
    public final static String FILE_BREAK = "\\";

    public static class Path {
        public static final String TONG_F = "\\\\HUNG_NAS\\Server_2020\\MAY TINH BAN_1\\TONG_F\\";
        public static final String BASE_URL = "\\\\HUNG_NAS\\Server_2020\\LE HOANG DUNG\\Auto2.0\\";
        public static final String CHU_KY_MAU = BASE_URL + "Chu ky mau";
    }

    public static class Resource {
        public static final String XLSX_TT_CHUAN = "\\\\HUNG_NAS\\Server_2020\\MAY TINH BAN_1\\THONG TIN CHUAN.xlsx";
        public final static String XLSX_USER = "\\\\HUNG_NAS\\Server_2020\\LE HOANG DUNG\\Auto2.0\\Data\\User.xlsx";
    }

    public static class Page {

    }

    public static class Extension {
        public static final String JPG = ".jpg";
        public static final String FXML = ".fxml";
        public static final String XLSX = ".xlsx";
        public static final String LSX = ".lsx";

    }

    public static FXMLLoader openFXML(String sour) {
        return new FXMLLoader(getResource(FXML_PREFIX + sour + Extension.FXML));
    }

    public static class InputType {
        public static final int SIM_FILE = 0;
        public static final int CUSTOMER_FILE = 1;
        public static final int OLD_FILE = 2;
    }

    private static URL getResource(String s) {
        return Conts.class.getResource(s);
    }
}
