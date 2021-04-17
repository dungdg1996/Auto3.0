package app.entity;

import java.util.Objects;

public class ConfigData {
    private int index;
    private String loai;
    private String giaTri;
    private int x;
    private int y;
    private String font;
    private int size;
    private String mau;
    private String canLe;
    private String kieuChu;
    private String style;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(String giaTri) {
        this.giaTri = giaTri;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public String getCanLe() {
        return canLe;
    }

    public void setCanLe(String canLe) {
        this.canLe = canLe;
    }

    public String getKieuChu() {
        return kieuChu;
    }

    public void setKieuChu(String kieuChu) {
        this.kieuChu = kieuChu;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigData that = (ConfigData) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                "index=" + index +
                ", loai='" + loai + '\'' +
                ", giaTri='" + giaTri + '\'' +
                ", xs=" + x +
                ", y=" + y +
                ", font='" + font + '\'' +
                ", size=" + size +
                ", mau='" + mau + '\'' +
                ", canLe='" + canLe + '\'' +
                ", kieuChu='" + kieuChu + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}
