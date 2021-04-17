package app.validation;

import app.model.Customer;
import app.model.MaTinh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Validator {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final String NAME_REGEX = "\\D+";
    public static final String DATE_REGEX = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
    public static final String ID_NUMBER_REGEX = "^(?=[0-9]*$)(?:.{9}|.{12})$";
    public static final String CDD_REGEX = "CDD|CXN|CQH";

    public static void checkValid(Customer customer) throws ParseException {
        if (invalidName(customer.getHoVaTen()))
            throw new ValidationException("Tên khách hàng không hợp lệ!");
        if (invalidDate(customer.getNgaySinh()))
            throw new ValidationException("Ngày sinh không hợp lệ!");
        if (invalidId(customer.getSoGiayTo()))
            throw new ValidationException("Số giấy tờ không hợp lệ!");
        if (invalidDate(customer.getNgayCap()))
            throw new ValidationException("Ngày cấp không hợp lệ!");
        if (sdf.parse(customer.getNgaySinh()).compareTo(sdf.parse(customer.getNgayCap())) >= 0)
            throw new ValidationException("Ngày sinh phải nhỏ hơn ngày cấp giấy tờ!");
        if (diffYear(customer.getNgaySinh()) <= 14)
            throw new ValidationException("Khách hàng phải từ 14 tuổi trở lên!");
        if (customer.getLoaiGiayTo().equals("CCCD") && customer.getSoGiayTo().length() == 9)
            throw new ValidationException("Căn cước phải có 12 số!");
        if (customer.getLoaiGiayTo().equals("CCCD") && !customer.getMaTinh().matches(CDD_REGEX))
            throw new ValidationException("Thẻ căn cước có nơi cấp không hợp lệ!");
        if (!MaTinh.exits(customer.getMaTinh()))
            throw new ValidationException("Sai mã nơi cấp!");
        String tinh = customer.getNoiCap();
        if(customer.getDiaChi() == null || customer.getDiaChi().isEmpty())
            throw new ValidationException("Bạn chưa nhập địa chỉ!");
        if (!customer.getLoaiGiayTo().equals("CDD") && !customer.getDiaChi().endsWith(tinh.toUpperCase()) && !customer.getMaTinh().matches(CDD_REGEX))
            throw new ValidationException("Sai địa chỉ, địa chỉ phải thuộc tỉnh " + tinh);
        String id = MaTinh.getId(customer.getMaTinh());
        if (!id.equals("*") && !customer.getSoGiayTo().startsWith(id))
            throw new ValidationException("Số giấy tờ không thuộc " + tinh);
    }

    private static boolean invalidId(String s) {
        return s == null || !s.matches(ID_NUMBER_REGEX);
    }

    private static boolean invalidName(String s) {
        return s == null || !s.matches(NAME_REGEX);
    }

    private static boolean invalidDate(String s) {
        return s == null || !s.matches(DATE_REGEX);
    }

    private static int diffYear(String s1) throws ParseException {
        Date firstDate = sdf.parse(s1);
        Date secondDate = new Date();
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) (diff / 365);
    }
}
