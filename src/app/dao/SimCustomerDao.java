package app.dao;

import app.Conts;
import app.model.Customer;
import app.model.Sim;
import app.utils.FileUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static app.utils.ExcelUtils.getCellStringValue;

public class SimCustomerDao {
    private ArrayList<Customer> customers;
    private final String filePath;

    public SimCustomerDao(String filePath) {
        this.filePath = filePath;
    }

    public boolean exits(String soGiayTo) {
        fetch();
        return customers
                .stream()
                .anyMatch(c -> c.getSoGiayTo().equals(soGiayTo));
    }

    private void fetch() {
        ZipSecureFile.setMinInflateRatio(0);
        customers = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(1);
            if (sheet == null) return;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                if (row.getCell(0) == null) row.createCell(0);
                if (row.getCell(0).toString().isEmpty()) {
                    continue;
                }
                Customer customer = new Customer();
                customer.setIndex(i);
                mapTo(customer, row);
                customers.add(customer);
            }
            workbook.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found: " + filePath);
        }
    }

    public ArrayList<Customer> findAllCustomer() {
        fetch();
        return customers;
    }

    public void mapTo(Customer bean, Row row) {
        bean.setHoVaTen(getCellStringValue(row.getCell(0)));
        bean.setGioiTinh(getCellStringValue(row.getCell(1)));
        bean.setNgaySinh(getCellStringValue(row.getCell(2)));
        bean.setSoGiayTo(getCellStringValue(row.getCell(3)));
        bean.setMaTinh(getCellStringValue(row.getCell(5)));
        bean.setNgayCap(getCellStringValue(row.getCell(7)));
        bean.setDiaChi(getCellStringValue(row.getCell(8)));
        bean.setMaHinh(getCellStringValue(row.getCell(9)));
    }

    public ArrayList<Sim> findAllSim() {
        ArrayList<Sim> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return list;
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Sim sim = new Sim();
                sim.setId(i);
                String sdt = getCellStringValue(row.getCell(0));
                sim.setSdt(sdt.substring(sdt.length() - 9));
                sim.setSeri(getCellStringValue(row.getCell(1)));
                sim.setOtp(getCellStringValue(row.getCell(2)));
                if(row.getCell(3) != null) {
                    sim.setSoGiayTo(getCellStringValue(row.getCell(3)));
                }
                list.add(sim);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
