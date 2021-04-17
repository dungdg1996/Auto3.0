package app.controller;

import app.Conts;
import app.dao.UserDao;
import app.model.User;
import app.views.AlertLogger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private ComboBox<String> khuVuc;

    @FXML
    private TextField ten;

    @FXML
    private TextField pgd;

    @FXML
    private TextField diaChiPgd;

    @FXML
    private TextField sdtPgd;

    @FXML
    private TextField daiDienPgd;

    @FXML
    private TextField path;

    @FXML
    private Button chose;

    @FXML
    private Button pre;

    @FXML
    private Button next;

    @FXML
    private Button save;

    @FXML
    private Button delete;

    @FXML
    private ImageView img;

    @FXML
    private Label imgName;

    private final UserDao userDao = UserDao.getInstance();
    private final List<Image> images = new ArrayList<>();
    private Integer userId = null;
    private int imgIndex = 0;
    private File[] files;

    public void setUser(User user) {
        this.userId = user.getId();
        this.ten.setText(user.getTenGdv());
        this.pgd.setText(user.getPgd());
        this.diaChiPgd.setText(user.getDiaChiPgd());
        this.sdtPgd.setText(user.getDtPgd());
        this.daiDienPgd.setText(user.getDaiDienPgd());
        this.path.setText(user.getPath());

        if (Objects.isNull(userId)) {
            delete.setVisible(false);
        }

        loadImg();
    }

    private void loadImg() {
        if (path.getText() == null || path.getText().isEmpty()) return;
        File file = new File(path.getText());
        files = file.listFiles();
        if (files == null || files.length == 0) return;
        try {
            images.clear();
            for (File f : files) {
                Image image = new Image(new FileInputStream(f));
                images.add(image);
            }
            imgIndex = 0;
            img.setImage(images.get(imgIndex));
            imgName.setText(files[imgIndex].getName());
        } catch (Exception e) {
            AlertLogger.getInstance().show(e.getMessage());
        }
    }

    @FXML
    public void next(Event event) {
        if (images.size() == 0) return;
        imgIndex++;
        if (imgIndex == images.size()) imgIndex = 0;
        imgName.setText(files[imgIndex].getName());
        img.setImage(images.get(imgIndex));
    }

    @FXML
    public void pre(Event event) {
        imgIndex--;
        if (imgIndex == -1) imgIndex += images.size();
        imgName.setText(files[imgIndex].getName());
        img.setImage(images.get(imgIndex));
    }

    @FXML
    public void choseFolder(Event event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(Conts.Path.BASE_URL + "MAU"));
        chooser.setTitle("Chọn thư mục mẫu");
        File file = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) path.setText(file.getPath());
        loadImg();
    }

    @FXML
    public void save(Event event) {
        User user = new User();
        user.setKhuVuc(khuVuc.getSelectionModel().getSelectedItem());
        user.setId(userId);
        user.setTenGdv(ten.getText());
        user.setPgd(pgd.getText());
        user.setDiaChiPgd(diaChiPgd.getText());
        user.setDtPgd(sdtPgd.getText());
        user.setDaiDienPgd(daiDienPgd.getText());
        user.setPath(path.getText());

        String valid = valid(user);
        if (valid.isEmpty()) {
            try {
                userDao.save(user);
                AlertLogger.getInstance().show(Alert.AlertType.INFORMATION, "Thêm thành công");
                ((Node) event.getSource()).getScene().getWindow().hide();
            } catch (Exception e) {
                e.printStackTrace();
                AlertLogger.getInstance().show(e.getMessage());
            }
        } else AlertLogger.getInstance().show(valid);

    }

    private String valid(User user) {
        if (user.getPath() == null || user.getPath().isEmpty()) return "Bạn chưa chọn file mẫu";
        if (user.getTenGdv() == null || user.getTenGdv().isEmpty()) return "Tên user không được trống";
        if (user.getPgd() == null || user.getPgd().isEmpty()) return "Bạn chưa nhập tên PGD";
        if (user.getDiaChiPgd() == null || user.getDiaChiPgd().isEmpty()) return "Bạn chưa nhập địa chỉ PGD";
        if (user.getDtPgd() == null || user.getDtPgd().isEmpty()) return "Bạn chưa nhập SDT PGD";
        if (user.getDaiDienPgd() == null || user.getDaiDienPgd().isEmpty()) return "Bạn chưa nhập tên GDV";
        return "";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        khuVuc.setItems(FXCollections.observableArrayList("HCM", "HYN"));
        khuVuc.getSelectionModel().select(0);
    }

    public void delete(ActionEvent e) {
        ButtonType rs = AlertLogger.getInstance().show("Bạn có muốn xoá mẫu này?");
        if (rs == ButtonType.OK) {
            userDao.delete(userId);
            userDao.commit();
            AlertLogger.getInstance().show("Hãy restart lại chương trình để áp dụng thay đổi");
            ((Node) e.getSource()).getScene().getWindow().hide();
        }
    }
}
