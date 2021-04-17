package app.model;
public class User {
    private Integer id;
    private String tenGdv;
    private String khuVuc;
    private String pgd;
    private String diaChiPgd;
    private String dtPgd;
    private String daiDienPgd;
    private String path;

    @Override
    public String toString() {
        return tenGdv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenGdv() {
        return tenGdv;
    }

    public void setTenGdv(String tenGdv) {
        this.tenGdv = tenGdv;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    public String getPgd() {
        return pgd;
    }

    public void setPgd(String pgd) {
        this.pgd = pgd;
    }

    public String getDiaChiPgd() {
        return diaChiPgd;
    }

    public void setDiaChiPgd(String diaChiPgd) {
        this.diaChiPgd = diaChiPgd;
    }

    public String getDtPgd() {
        return dtPgd;
    }

    public void setDtPgd(String dtPgd) {
        this.dtPgd = dtPgd;
    }

    public String getDaiDienPgd() {
        return daiDienPgd;
    }

    public void setDaiDienPgd(String daiDienPgd) {
        this.daiDienPgd = daiDienPgd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
