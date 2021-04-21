package app.utils;

import app.Conts;
import app.Main;
import javafx.fxml.FXMLLoader;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static app.Conts.DateFormat.*;
import static app.Conts.FXML_PREFIX;

public class AppUtils {

    public static Map<String, String> getAppConfigData(String fileName) {
        Map<String, String> data = new HashMap<>();
        try {
            FileInputStream is = new FileInputStream(fileName);
            Workbook wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheet("data");
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                String key = ExcelUtils.getCellStringValue(sheet.getRow(i), 0);
                String value = ExcelUtils.getCellStringValue(sheet.getRow(i), 1);
                if (key.isEmpty()) continue;
                data.put(key, value);
            }
            wb.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

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

    public static FXMLLoader openFXML(String sour) {
        return new FXMLLoader(getResource(FXML_PREFIX + sour + Conts.Extension.FXML));
    }

    private static URL getResource(String s) {
        return Main.class.getResource(s);
    }
}
