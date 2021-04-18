package app;

import java.text.SimpleDateFormat;


public class Conts {

    public final static String FXML_PREFIX = "/fxml/";
    public final static String FILE_BREAK = "\\";

    public static class DateFormat {
        public static final SimpleDateFormat DD = new SimpleDateFormat("dd");
        public static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
        public static final SimpleDateFormat MM = new SimpleDateFormat("MM");
    }

    public static class Path {
        public static final String TONG_F = System.getProperty("$TONG_F$") + "\\";
        public static final String CHU_KY_MAU = System.getProperty("$CHU_KY_MAU$");
        public static final String ANH_HD_MAU = System.getProperty("$ANH_HD_MAU$");
    }

    public static class File {
        public static final String XLSX_TT_CHUAN = System.getProperty("$THONG_TIN_CHUAN_XLSX$");
        public final static String XLSX_USER = System.getProperty("$USER_XLSX$");
        public final static String XLS_CCBS = System.getProperty("$CCBS_MAU_XLS$");
    }

    public static class Page {

    }

    public static class Extension {
        public static final String JPG = ".jpg";
        public static final String FXML = ".fxml";
        public static final String XLSX = ".xlsx";
        public static final String LSX = ".lsx";

    }

    public static class InputType {
        public static final int SIM_FILE = 0;
        public static final int CUSTOMER_FILE = 1;
        public static final int OLD_FILE = 2;
    }
}
