import app.Main;
import app.utils.AppUtils;

import java.util.Map;

public class Launcher {
    public static void main(String[] args) {
        Map<String, String> appConfigData = AppUtils.getAppConfigData("appConfig.xlsx.");
        appConfigData.forEach(System::setProperty);
        Main.main(args);
    }
}
