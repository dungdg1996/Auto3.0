package app.utils;

import app.Conts;
import app.model.Customer;
import app.model.Sim;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static String cellToString(Cell cell) {
        return getStringValue(cell);
    }

    public static String getStringValue(Cell cell) {
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

    public static String getStringValue(Row row, int cellIndex) {
        if (row == null) return "";
        return getStringValue(row.getCell(cellIndex));
    }

    public static int getIntValue(Row row, int cellIndex) {
        return getIntValue(row.getCell(cellIndex));
    }

    private static int getIntValue(Cell cell) {
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

    public static void setStringValue(Row row, int index, String value) {
        if (row == null) return;
        row.createCell(index, CellType.STRING).setCellValue(value);
    }

    /**
     * Remove a row by its index
     *
     * @param sheet    a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(Sheet sheet, int rowIndex) {
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

    public static int generateNewRow(Sheet sheet) {
        return 0;
    }

    public static int getLastIndex(Sheet sheet) {
        return 0;
    }

    public static class CcbsExcel {
        public static void creat(List<Map<String, String>> data, String path) {

            try {
                FileInputStream is = new FileInputStream(Conts.Path.BASE_URL + "CCBS.xls");
                Workbook wb = new HSSFWorkbook(is);
                Sheet sheet = wb.getSheet("Sheet1");
                int index = 0;
                for (Map<String, String> map : data) {
                    Row row = sheet.createRow(++index);
                    setStringValue(row, 0, map.get("$sdt$"));
                    setStringValue(row, 1, map.get("$seri$") + ".zip");
                    setStringValue(row, 2, map.get("$ten$"));
                    setStringValue(row, 3, map.get("$gioiTinh$"));
                    setStringValue(row, 4, map.get("$ngaySinh$"));
                    setStringValue(row, 5, map.get("$soGiayTo$"));
                    setStringValue(row, 6, map.get("$maTinh$"));
                    setStringValue(row, 7, map.get("$ngayCap$"));
                    setStringValue(row, 8, map.get("$diaChi$"));
                    setStringValue(row, 9, map.get("$maHinh1$"));
                    setStringValue(row, 10, map.get("$maHinh2$"));
                    setStringValue(row, 11, map.get("$maHinh3$"));
                    setStringValue(row, 12, map.get("$maHinh4$"));
                    setStringValue(row, 13, map.get("$pgd$"));
                    setStringValue(row, 14, map.get("$diaChiPgd$"));
                    setStringValue(row, 15, map.get("$dtPgd$"));
                    setStringValue(row, 16, map.get("$daiDienPgd$"));
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
    }
}
