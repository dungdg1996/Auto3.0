package app.dao;

import app.Conts;
import app.model.User;
import app.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    private static final UserDao instance = new UserDao();

    public static UserDao getInstance() {
        return instance;
    }

    private Workbook workbook;
    private Sheet sheet;
    private final List<User> users;

    private UserDao() {
        users = new ArrayList<>();
        load();
    }

    private void load() {
        users.clear();
        try {
            workbook = new XSSFWorkbook(new FileInputStream(Conts.Resource.XLSX_USER));
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found: " + Conts.Resource.XLSX_USER);
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            User user = new User();
            user.setId(i);
            user.setKhuVuc(row.getCell(0).getStringCellValue());
            user.setTenGdv(row.getCell(1).getStringCellValue());
            user.setPgd(row.getCell(2).getStringCellValue());
            user.setDiaChiPgd(row.getCell(3).getStringCellValue());
            user.setDtPgd(row.getCell(4).getStringCellValue());
            user.setDaiDienPgd(row.getCell(5).getStringCellValue());
            user.setPath(row.getCell(6).getStringCellValue());
            users.add(user);
        }
    }


    public List<User> findAll() {
        return users;
    }

    public List<User> findByKhuVuc(String khuVuc) {
        return users.stream().filter(user -> user.getKhuVuc().equals(khuVuc))
                .collect(Collectors.toList());
    }

    public User save(User user) {
        Row row;
        if (user.getId() == null) {
            user.setId(users.size() + 1);
            row = sheet.createRow(user.getId());
            row.createCell(0).setCellValue(user.getKhuVuc());
            row.createCell(1).setCellValue(user.getTenGdv());
            row.createCell(2).setCellValue(user.getPgd());
            row.createCell(3).setCellValue(user.getDiaChiPgd());
            row.createCell(4).setCellValue(user.getDtPgd());
            row.createCell(5).setCellValue(user.getDaiDienPgd());
            row.createCell(6).setCellValue(user.getPath());
        } else {
            row = sheet.getRow(user.getId());
            row.getCell(0).setCellValue(user.getKhuVuc());
            row.getCell(1).setCellValue(user.getTenGdv());
            row.getCell(2).setCellValue(user.getPgd());
            row.getCell(3).setCellValue(user.getDiaChiPgd());
            row.getCell(4).setCellValue(user.getDtPgd());
            row.getCell(5).setCellValue(user.getDaiDienPgd());
            row.getCell(6).setCellValue(user.getPath());
        }
        commit();
        load();
        return user;
    }

    public void delete(int userId) {
        ExcelUtils.removeRow(sheet, userId);
    }

    public void commit() {
        try {
            FileOutputStream os = new FileOutputStream(Conts.Resource.XLSX_USER);
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file User.xlsx");
        }
    }


}
