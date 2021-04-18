package app.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case _NONE:
                return cell.toString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                return String.valueOf((int) cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell))
                        return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                    return String.valueOf((int) cell.getNumericCellValue());
                } else if (cell.getCachedFormulaResultType() == CellType.STRING)
                    return cell.getStringCellValue();
            case BOOLEAN:
                if (cell.getBooleanCellValue()) return "true";
                return "false";
            case ERROR:
                return "ERRO";
        }
        return "";
    }

    public static String getCellStringValue(Row row, int cellIndex) {
        if (row == null) return "";
        return getCellStringValue(row.getCell(cellIndex));
    }

    public static int getCellIntValue(Row row, int cellIndex) {
        return getCellIntValue(row.getCell(cellIndex));
    }

    private static int getCellIntValue(Cell cell) {
        if (cell == null) return -1;
        switch (cell.getCellType()) {
            case _NONE:
                return 0;
            case FORMULA:
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                return Integer.parseInt(cell.getStringCellValue());
            case BOOLEAN:
                if (cell.getBooleanCellValue()) return 1;
                return 0;
            case ERROR:
                return -1;
        }
        return -1;
    }

    public static void setCellStringValue(Row row, int index, String value) {
        if (row == null) return;
        row.createCell(index, CellType.STRING).setCellValue(value);
    }

    /**
     * Remove a row by its index
     *
     * @param sheet    a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void deleteRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
    }

    public static void createFileWithTemplate(String template, List<Map<String, String>> data, String path) {
        try {
            FileInputStream is = new FileInputStream(template);
            Workbook wb;
            if (template.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else if (template.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                throw new RuntimeException("File CCBS mẫu không hợp lệ");
            }
            Sheet sheet = wb.getSheet("Sheet1");
            Map<String, Integer> mappingData = getMappingData(sheet.getRow(1));
            int index = 0;
            for (Map<String, String> map : data) {
                Row row = sheet.createRow(++index);
                if (mappingData.containsKey("$index$")) {
                    setCellStringValue(row, mappingData.get("$index$"), String.valueOf(index));
                }
                mappingData.forEach((s, cellIndex) -> {
                    setCellStringValue(row, cellIndex, StringUtils.replaceString(s, map));
                });
            }
            File file = new File(path + "CCBS_.xls");
            FileOutputStream fou = new FileOutputStream(file);
            wb.write(fou);
            wb.close();
            is.close();
            fou.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> getMappingData(Row row) {
        Map<String, Integer> map = new HashMap<>();
        if (row == null) {
            return map;
        }
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i <= lastCellNum; i++) {
            map.put(ExcelUtils.getCellStringValue(row, i), i);
        }
        return map;
    }
}
