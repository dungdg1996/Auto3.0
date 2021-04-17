package app.controller;

import app.Conts;
import app.model.Customer;
import app.model.MaTinh;
import app.utils.FileUtils;
import app.validation.Validator;
import app.views.AlertLogger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static app.AppContext.danhSachController;

public class CreateCustomerController {

    @FXML
    private TextField tf_ten;
    @FXML
    private CheckBox cb_nam;
    @FXML
    private CheckBox cb_nu;
    @FXML
    private DatePicker dp_ngaySinh;
    @FXML
    private ComboBox<String> cbb_loaiGiayTo;
    @FXML
    private TextField tf_soGiayTo;
    @FXML
    private DatePicker dp_ngayCap;
    @FXML
    private TextField tf_maNoiCap;
    @FXML
    private TextField tf_diaChi;
    @FXML
    private Button btn_save, btn_clear;
    @FXML
    private Label lb_tinh, lb_checkStatus, lb_maHinh;
    @FXML
    private VBox vBox;
    @FXML
    private ImageView img_1, img_2, img_3;
    @FXML
    private AnchorPane pane_img;
    @FXML
    private Button btn_check;

    private final ArrayList<ImageView> imageViews = new ArrayList<>();
    private final List<File> files = new ArrayList<>();

    private final AlertLogger logger = AlertLogger.getInstance();

    @FXML
    public void initialize() {
        btn_save.setOnAction(this::saveCustomer);
        btn_clear.setOnAction(this::clear);
        btn_check.setOnAction(this::checkCustomerID);
        tf_maNoiCap.textProperty().addListener(this::showMaTinh);
        vBox.setOnDragOver(this::acceptToDragFile);
        vBox.setOnDragDropped(this::checkImage);

        autoCompleteDatePick(dp_ngayCap);
        autoCompleteDatePick(dp_ngaySinh);
        imageViews.add(img_1);
        imageViews.add(img_2);
        imageViews.add(img_3);
        for (ImageView imageView : imageViews)
            imageView.fitHeightProperty().bind(pane_img.heightProperty());
        cb_nam.setOnAction(event -> cb_nu.setSelected(!cb_nam.isSelected()));
        cb_nu.setOnAction(event -> cb_nam.setSelected(!cb_nu.isSelected()));
        cbb_loaiGiayTo.getItems().addAll("CMND", "CCCD");
        cbb_loaiGiayTo.getSelectionModel().select(0);
        cb_nam.setSelected(true);
    }

    private void showMaTinh(Observable observable, String old, String newValue) {
        tf_maNoiCap.setText(newValue.toUpperCase());
        lb_tinh.setText(MaTinh.getName(newValue.toUpperCase()));
        lb_tinh.setStyle("-fx-text-fill: green");
    }

    private void checkImage(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            for (File file : db.getFiles())
                addImage(file);
        }
        event.setDropCompleted(false);
        event.consume();
    }

    private void acceptToDragFile(DragEvent event) {
        if (event.getGestureSource() != vBox
                && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private Boolean running = false;

    private void saveCustomer(ActionEvent event) {
        if (running) return;
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Customer customer = getCustomer();
                try {
                    Validator.checkValid(customer);
                    Platform.runLater(() -> {
                        lb_checkStatus.setStyle("-fx-text-fill: green");
                        lb_checkStatus.setText("Đang lưu...");
                    });
                    MainController.customerDao.save(customer);
                    Platform.runLater(() -> {
                        lb_maHinh.setText(customer.getMaHinh());
                    });
                    FileUtils.save(customer.getMaHinh(), files);
                    Platform.runLater(() -> {
                        lb_checkStatus.setText("Lưu thông tin thành công");
                        logger.show(Alert.AlertType.INFORMATION, "Lưu thông tin khách hàng thành công");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        lb_checkStatus.setStyle("-fx-text-fill: red");
                        lb_checkStatus.setText(e.getMessage());
                    });
                }
                return null;
            }
        };
        task.setOnSucceeded(event1 -> {
            running = false;
            danhSachController.loadData();
        });
        running = true;
        new Thread(task).start();

    }

    private void clear(ActionEvent event) {
        tf_ten.setText("");
        cb_nam.setSelected(true);
        cb_nu.setSelected(false);
        dp_ngaySinh.getEditor().setText("");
        cbb_loaiGiayTo.getSelectionModel().select("CMND");
        tf_soGiayTo.setText("");
        dp_ngayCap.getEditor().setText("");
        tf_maNoiCap.setText("");
        tf_diaChi.setText("");
        lb_tinh.setText("");
        lb_maHinh.setText("");
        lb_checkStatus.setText("");
        files.clear();
        for (ImageView imageView : imageViews) {
            imageView.setImage(new Image("/resources/image/Untitled-1.jpg"));
        }
        files.clear();
        System.gc();
    }

    private void checkCustomerID(ActionEvent event) {
        if (MainController.customerDao.exits(tf_soGiayTo.getText())) {
            lb_checkStatus.setStyle("-fx-text-fill: red");
            lb_checkStatus.setText("Trùng CMND");
        } else {
            lb_checkStatus.setStyle("-fx-text-fill: green");
            lb_checkStatus.setText("CMND chưa nhập");
        }
    }

    private void autoCompleteDatePick(DatePicker datePicker) {
        datePicker.getEditor().setOnKeyTyped(event -> {
            if (!"0123456789".contains(event.getCharacter())) return;
            String text = datePicker.getEditor().getText();
            if ((text.length() == 3 || text.length() == 6)) {
                datePicker.getEditor()
                        .setText(text.substring(0, text.length() - 1) + "/" +
                                text.substring(text.length() - 1));
                datePicker.getEditor().end();
            }
        });
    }

    private void addImage(File file) {
        if (files.size() == 3)
            files.clear();
        Image image = null;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageViews.get(files.size()).setImage(image);
        files.add(file);
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setHoVaTen(tf_ten.getText().toUpperCase().trim().toUpperCase());
        customer.setGioiTinh(cb_nam.isSelected() ? "NAM" : "NỮ");
        customer.setNgaySinh(dp_ngaySinh.getEditor().getText());
        customer.setLoaiGiayTo(cbb_loaiGiayTo.getSelectionModel().getSelectedItem());
        customer.setSoGiayTo(tf_soGiayTo.getText().trim());
        customer.setNgayCap(dp_ngayCap.getEditor().getText());
        customer.setMaTinh(tf_maNoiCap.getText().trim());
        String diaChi = tf_diaChi.getText().trim().toUpperCase()
                .replaceAll("KHÁNH HOÀ", "KHÁNH HÒA")
                .replaceAll("ĐẮK LẮK", "ĐẮC LẮC")
                .replaceAll("ĐẮK NÔNG", "ĐẮC NÔNG")
                .replaceAll("THANH HÓA", "THANH HOÁ");
        customer.setDiaChi(diaChi);
        customer.setMaHinh(lb_maHinh.getText());
        return customer;
    }

    public void init(Object object) {
        Customer customer = (Customer) object;
        tf_ten.setText(customer.getHoVaTen());
        boolean male = customer.getGioiTinh().toUpperCase().equals("NAM");
        cb_nam.setSelected(male);
        cb_nu.setSelected(!male);
        dp_ngaySinh.getEditor().setText(customer.getNgaySinh());
        cbb_loaiGiayTo.getSelectionModel().select((customer.getMaTinh().equals("CDD")) ? "CCCD" : "CMND");
        tf_soGiayTo.setText(customer.getSoGiayTo());
        dp_ngayCap.getEditor().setText(customer.getNgayCap());
        tf_maNoiCap.setText(customer.getMaTinh());
        tf_diaChi.setText(customer.getDiaChi());
        lb_tinh.setText(customer.getNoiCap());
        lb_maHinh.setText(customer.getMaHinh());
        lb_checkStatus.setText("");
        for (int i = 0; i < 3; i++) {
            File file = new File(Conts.Path.TONG_F + "\\" + customer.getMaHinh() + (i + 1) + ".jpg");
            if (!file.exists()) continue;
            addImage(file);
        }
    }
}
