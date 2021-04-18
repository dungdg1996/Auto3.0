package app.dao;

import app.entity.ConfigData;
import app.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigDao {
    private final String filePath;
    private final List<String> listImg;

    public ConfigDao(String filePath){
        this.filePath = filePath;
        this.listImg = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheet("Tong");
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String stringValue = ExcelUtils.getCellStringValue(row, 0);
                if (stringValue != null && !stringValue.isBlank()) listImg.add(stringValue);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getListImg() {
        return listImg;
    }

    private ConfigData parse(Row row) {
        if (row.getCell(0) == null) return null;
        ConfigData configData = new ConfigData();
        configData.setLoai(ExcelUtils.getCellStringValue(row, 0));
        configData.setGiaTri(ExcelUtils.getCellStringValue(row, 1));
        configData.setX(ExcelUtils.getCellIntValue(row, 2));
        configData.setY(ExcelUtils.getCellIntValue(row, 3));
        configData.setFont(ExcelUtils.getCellStringValue(row, 4));
        configData.setSize(ExcelUtils.getCellIntValue(row, 5));
        configData.setMau(ExcelUtils.getCellStringValue(row, 6));
        configData.setCanLe(ExcelUtils.getCellStringValue(row, 7));
        configData.setKieuChu(ExcelUtils.getCellStringValue(row, 8));
        configData.setStyle(ExcelUtils.getCellStringValue(row, 9));
        return configData;
    }

    public ArrayList<ConfigData> getData(String sheetName) {
        ArrayList<ConfigData> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheet(sheetName);
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                ConfigData parse = parse(row);
                if (parse != null) {
                    parse.setIndex(i);
                    list.add(parse);
                }
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
