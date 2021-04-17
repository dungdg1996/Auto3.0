package app.dao;

import app.Conts;
import app.model.BaseEntity;
import app.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generic DAO class for xlsx file
 * @param <E> type of entity
 */
public abstract class AbstractExcelDao<E extends BaseEntity> {

    private Workbook workbook;
    private Sheet sheet;
    private List<E> entities;
    private final String filePath;
    private int startIndex = 1;

    /**
     * Load excel file from file path
     * @param filePath path to xlsx file
     */
    public AbstractExcelDao(String filePath) {
        this.filePath = filePath;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            sheet = workbook.getSheetAt(0);
            entities = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found: " + filePath);
        }
    }

    /**
     * @return Class the class of entity
     */
    @SuppressWarnings ("unchecked")
    public Class<E> getTypeParameterClass()
    {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<E>) paramType.getActualTypeArguments()[0];
    }


    /**
     * mapping field in entity to excel row
     * @param entity list if entity
     * @param row the row of excel file
     */
    public abstract void mappingEntityToRow(E entity, Row row);

    public abstract void mappingRowToEntity(Row row, E entity);

    /**
     * Load data from sheet
     */
    public void loadData() {
        entities.clear();
        int lastIndex = ExcelUtils.getLastIndex(sheet);
        try {
            for (int i = startIndex; i <= lastIndex; i++) {
                Row row = sheet.getRow(i);
                E entity = getTypeParameterClass().newInstance();
                mappingRowToEntity(row, entity);
                entities.add(entity);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Get all data from excel file
     * @return list of entity
     */
    public List<E> findAll() {
        loadData();
        return entities;
    }

    /**
     * @param id id of entity
     * @return entity has this id
     */
    public E findById(Integer id) {
        loadData();
        return entities.stream()
                .filter(e -> e.getId().equals(id))
                .findAny().orElse(null);
    }

    /**
     * Save entity to excel file
     * @param entity
     */
    public void save(E entity) {
        Row row = null;

        if (!Objects.isNull(entity.getId())) {
            row = sheet.getRow(entity.getId());
        }

        if (Objects.isNull(row)) {
            int rowId = ExcelUtils.generateNewRow(sheet);
            row = sheet.createRow(rowId);
        }

        mappingEntityToRow(entity, row);
    }

    /**
     * Delete entity
     * @param entity
     * @return
     */
    public boolean delete(E entity) {
        E current = findById(entity.getId());
        if (!entity.equals(current)) {
            return false;
        }
        ExcelUtils.removeRow(sheet, entity.getId());
        return true;
    }

    /**
     * Save all change to excel file
     */
    public void commit() {
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file User.xlsx");
        }
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
