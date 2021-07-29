import app.Main;
import app.utils.AppUtils;

import java.util.Map;

public class Launcher {
    public static void main(String[] args) {
//        Map<String, String> appConfigData = AppUtils.getAppConfigData("\\\\hung_nas\\Server_2020\\LE HOANG DUNG\\Auto2.0\\autoConfig.xlsx");
        Map<String, String> appConfigData = AppUtils.getAppConfigData(System.getProperty("user.home") + "\\autoConfig.xlsx");
//        Map<String, String> appConfigData = AppUtils.getAppConfigData("autoConfig.xlsx");
        appConfigData.forEach(System::setProperty);
        appConfigData.forEach((k, v) -> System.out.println(k + " = " + v));
        Main.main(args);
    }
}
