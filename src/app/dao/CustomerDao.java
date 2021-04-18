package app.dao;

import app.model.Customer;
import app.utils.FileUtils;
import app.Conts;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

import static app.utils.ExcelUtils.getCellStringValue;

public class CustomerDao {
    private Map<Integer, String> emptyRows;
    private ArrayList<Customer> customers;
    private final String filePath;
    private int firstIndex;
    private String sheetName = "Sheet1";

    public CustomerDao() {
        this(Conts.File.XLSX_TT_CHUAN);
        this.firstIndex = 1;
        this.sheetName = "CHUAN";
    }

    public CustomerDao(String filePath) {
        this.filePath = filePath;
        this.firstIndex = 0;
    }

    public boolean exits(String soGiayTo) {
        fetch();
        return customers
                .stream()
                .anyMatch(c -> c.getSoGiayTo().equals(soGiayTo));
    }

    private void fetch() {
        ZipSecureFile.setMinInflateRatio(0);
        emptyRows = new LinkedHashMap<>();
        customers = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            for (int i = firstIndex; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                if (row.getCell(0) == null) row.createCell(0);
                if (row.getCell(0).toString().isEmpty()) {
                    if (row.getCell(9) != null) emptyRows.put(i, row.getCell(9).toString());
                    continue;
                }
                Customer customer = new Customer();
                customer.setIndex(i);
                mapTo(customer, row);
                customers.add(customer);
                workbook.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found: " + filePath);
        }
    }

    public ArrayList<Customer> findAll() {
        fetch();
        return customers;
    }

    public void mapTo(Customer bean, Row row) {
        bean.setHoVaTen(getCellStringValue(row.getCell(0)));
        bean.setGioiTinh(getCellStringValue(row.getCell(1)));
        bean.setNgaySinh(getCellStringValue(row.getCell(2)));
        bean.setSoGiayTo(getCellStringValue(row.getCell(3)));
        //customer.setTen(cellToString(row.getCell(4)));
        bean.setMaTinh(getCellStringValue(row.getCell(5)));
        //customer.setTen(stringOf(row.getCell(6)));
        bean.setNgayCap(getCellStringValue(row.getCell(7)));
        bean.setDiaChi(getCellStringValue(row.getCell(8)));
        bean.setMaHinh(getCellStringValue(row.getCell(9)));
    }

    public void save(Customer customer) {
        try {
            if (exits(customer.getSoGiayTo()))
                throw new RuntimeException("Trùng số giấy tờ!");
            if (emptyRows.isEmpty())
                throw new RuntimeException("File THONG_TIN_CHUAN đã đầy, hãy thêm mã hình và thử lại!");
            FileInputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            font.setBold(false);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            String maHinh;
            int index;
            if (!exits(customer.getSoGiayTo())) {
                Map.Entry<Integer, String> entry = emptyRows.entrySet().iterator().next();
                index = entry.getKey();
                maHinh = entry.getValue();
            } else {
                Customer exitsCustomer = findByMaHinh(customer.getSoGiayTo());
                index = exitsCustomer.getIndex();
                maHinh = exitsCustomer.getMaHinh();
            }
            Row row = sheet.getRow(index);
            customer.setMaHinh(maHinh);
            row.createCell(0).setCellValue(customer.getHoVaTen());
            row.getCell(0).setCellStyle(style);
            row.createCell(1).setCellValue(customer.getGioiTinh());
            row.getCell(1).setCellStyle(style);
            row.createCell(2).setCellValue(customer.getNgaySinh());
            row.getCell(2).setCellStyle(style);
            row.createCell(3).setCellValue(customer.getSoGiayTo());
            row.getCell(3).setCellStyle(style);
            row.createCell(4).setCellValue("");
            row.getCell(4).setCellStyle(style);
            row.createCell(5).setCellValue(customer.getMaTinh());
            row.getCell(5).setCellStyle(style);
            row.createCell(6).setCellValue(customer.getLoaiGiayTo());
            row.getCell(6).setCellStyle(style);
            row.createCell(7).setCellValue(customer.getNgayCap());
            row.getCell(7).setCellStyle(style);
            row.createCell(8).setCellValue(customer.getDiaChi());
            row.getCell(8).setCellStyle(style);
            row.createCell(9).setCellValue(customer.getMaHinh());
            row.getCell(9).setCellStyle(style);
            save(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Gặp lỗi khi lưu file, đóng file THONG TIN CHUAN trước khi lưu!");
        }
        fetch();
    }

    private Customer findByMaHinh(String soGiayTo) {
        return customers.stream()
                .filter(customer -> customer.getMaHinh().equals(soGiayTo))
                .findAny().get();
    }

    private void save(Workbook workbook) throws IOException {
        backUp();
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void backUp() throws IOException {
        File sour = new File(filePath);
        File des = new File("D:\\Backup\\" + new Date().getTime() + "_" + sour.getName());
        File folder = new File("D:\\Backup");
        if (folder.exists()) folder.mkdirs();
        FileUtils.copy(sour, des);
    }
}
