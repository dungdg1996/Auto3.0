package app;

import app.dao.ConfigDao;
import app.entity.ConfigData;
import app.process.FormProcess;
import app.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        ArrayList<ConfigData> all = new ConfigDao("D:\\config.xlsx").getData("1.jpg");
        //all.forEach(System.out::println);
        FormProcess process = new FormProcess(all, "D:\\1.jpg");

        HashMap<String, String> data = new HashMap<>();
        data.put("$ten$", "Le Hoang Dung");
        data.put("$maHD$", "02131414");
        data.put("$sdtIndex$", "2");
        data.put("$sdt$", "0888725022");

        AppUtils.setSysTime(data);
        process.process(data);
        process.saveForm("D:\\100.jpg");
    }
}
