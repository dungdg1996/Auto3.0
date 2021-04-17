package app.model;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.Objects;

public class Customer {

    private int index;
    private String hoVaTen;
    private String ten;
    private String gioiTinh;
    private String ngaySinh;
    private String soGiayTo;
    private String maTinh;
    private String ngayCap;
    private String diaChi;
    private String maHinh;
    private String maHinh1;
    private String maHinh2;
    private String maHinh3;
    private String maHinh4;
    private String sdt;
    private String seri;
    private String maHD;
    private String pgd;
    private String dtPgd;
    private String diaChiPgd;
    private String daiDienPgd;
    private String ghiChu;
    private String otp;
    private String loaiGiayTo;
    private String noiCap;
    private String quocTich = "Việt Nam";
    private SimpleBooleanProperty selected;

    public Customer() {
        this.index = -1;
        this.maHinh = "";
        this.loaiGiayTo = "CMND";
        this.maHD = "Chờ xử lý";
        this.selected = new SimpleBooleanProperty(false);
    }

    public void merge(User user) {
        this.setDaiDienPgd(user.getDaiDienPgd());
        this.setDtPgd(user.getDtPgd());
        this.setDiaChiPgd(user.getDiaChiPgd());
        this.setPgd(user.getPgd());
    }


    public void setMaHinh(String maHinh) {
        this.maHinh = maHinh;
        this.maHinh1 = maHinh + 1;
        this.maHinh2 = maHinh + 2;
        this.maHinh3 = maHinh + 3;
        this.maHinh4 = maHinh + 4;
    }

    public String getNoiCap() {
        return MaTinh.getName(maTinh);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return soGiayTo.equals(customer.soGiayTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(soGiayTo);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
        this.ten = hoVaTen.substring(hoVaTen.lastIndexOf(" ") + 1);
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
    }

    public String getMaTinh() {
        return maTinh;
    }

    public void setMaTinh(String maTinh) {
        this.maTinh = maTinh;
        this.noiCap = MaTinh.getName(maTinh);
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(String ngayCap) {
        this.ngayCap = ngayCap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaHinh() {
        return maHinh;
    }

    public String getMaHinh1() {
        return maHinh1;
    }

    public void setMaHinh1(String maHinh1) {
        this.maHinh1 = maHinh1;
    }

    public String getMaHinh2() {
        return maHinh2;
    }

    public void setMaHinh2(String maHinh2) {
        this.maHinh2 = maHinh2;
    }

    public String getMaHinh3() {
        return maHinh3;
    }

    public void setMaHinh3(String maHinh3) {
        this.maHinh3 = maHinh3;
    }

    public String getMaHinh4() {
        return maHinh4;
    }

    public void setMaHinh4(String maHinh4) {
        this.maHinh4 = maHinh4;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getPgd() {
        return pgd;
    }

    public void setPgd(String pgd) {
        this.pgd = pgd;
    }

    public String getDtPgd() {
        return dtPgd;
    }

    public void setDtPgd(String dtPgd) {
        this.dtPgd = dtPgd;
    }

    public String getDiaChiPgd() {
        return diaChiPgd;
    }

    public void setDiaChiPgd(String diaChiPgd) {
        this.diaChiPgd = diaChiPgd;
    }

    public String getDaiDienPgd() {
        return daiDienPgd;
    }

    public void setDaiDienPgd(String daiDienPgd) {
        this.daiDienPgd = daiDienPgd;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }


    public void setNoiCap(String noiCap) {
        this.noiCap = noiCap;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public ObservableValue<Boolean> getSelected() {
        return this.selected;
    }
}
