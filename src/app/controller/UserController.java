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
        this.ten.setText(user.getTenFileMau());
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
        chooser.setInitialDirectory(new File(Conts.Path.ANH_HD_MAU));
        chooser.setTitle("Ch???n th?? m???c m???u");
        File file = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) path.setText(file.getPath());
        loadImg();
    }

    @FXML
    public void save(Event event) {
        User user = new User();
        user.setKhuVuc(khuVuc.getSelectionModel().getSelectedItem());
        user.setId(userId);
        user.setTenFileMau(ten.getText());
        user.setPgd(pgd.getText());
        user.setDiaChiPgd(diaChiPgd.getText());
        user.setDtPgd(sdtPgd.getText());
        user.setDaiDienPgd(daiDienPgd.getText());
        user.setPath(path.getText());

        String valid = valid(user);
        if (valid.isEmpty()) {
            try {
                userDao.save(user);
                AlertLogger.getInstance().show(Alert.AlertType.INFORMATION, "Th??m th??nh c??ng");
                ((Node) event.getSource()).getScene().getWindow().hide();
            } catch (Exception e) {
                e.printStackTrace();
                AlertLogger.getInstance().show(e.getMessage());
            }
        } else AlertLogger.getInstance().show(valid);

    }

    private String valid(User user) {
        if (user.getPath() == null || user.getPath().isEmpty()) return "B???n ch??a ch???n file m???u";
        if (user.getTenFileMau() == null || user.getTenFileMau().isEmpty()) return "T??n user kh??ng ???????c tr???ng";
        if (user.getPgd() == null || user.getPgd().isEmpty()) return "B???n ch??a nh???p t??n PGD";
        if (user.getDiaChiPgd() == null || user.getDiaChiPgd().isEmpty()) return "B???n ch??a nh???p ?????a ch??? PGD";
        if (user.getDtPgd() == null || user.getDtPgd().isEmpty()) return "B???n ch??a nh???p SDT PGD";
        if (user.getDaiDienPgd() == null || user.getDaiDienPgd().isEmpty()) return "B???n ch??a nh???p t??n GDV";
        return "";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        khuVuc.setItems(FXCollections.observableArrayList("HCM", "HYN"));
        khuVuc.getSelectionModel().select(0);
    }

    public void delete(ActionEvent e) {
        ButtonType rs = AlertLogger.getInstance().show("B???n c?? mu???n xo?? m???u n??y?");
        if (rs == ButtonType.OK) {
            userDao.delete(userId);
            userDao.commit();
            AlertLogger.getInstance().show("H??y restart l???i ch????ng tr??nh ????? ??p d???ng thay ?????i");
            ((Node) e.getSource()).getScene().getWindow().hide();
        }
    }
}
