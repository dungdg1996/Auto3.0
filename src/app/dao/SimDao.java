package app.dao;

import app.model.Sim;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static app.utils.ExcelUtils.cellToString;

public class SimDao {
    private final String filePath;
    private int sheetIndex = 1;

    public SimDao(String filePath){
        this.sheetIndex = 0;
        this.filePath = filePath;
    }

    public ArrayList<Sim> findAll() {
        ArrayList<Sim> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            int lastRow = sheet.getLastRowNum();
            for (int i = 0; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Sim sim = new Sim();
                sim.setId(i);
                String sdt = cellToString(row.getCell(0));
                sim.setSdt(sdt.substring(sdt.length() - 9));
                sim.setSeri(cellToString(row.getCell(1)));
                if (row.getCell(2) != null) sim.setOtp(cellToString(row.getCell(2)));
                if (row.getCell(3) != null) sim.setSoGiayTo(cellToString(row.getCell(3)));
                list.add(sim);
            }
            workbook.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
