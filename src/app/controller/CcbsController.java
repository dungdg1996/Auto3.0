package app.controller;

import app.Conts.InputType;
import app.Main;
import app.dao.CustomerDao;
import app.dao.SimCustomerDao;
import app.dao.SimDao;
import app.dao.UserDao;
import app.model.Customer;
import app.model.Sim;
import app.model.User;
import app.process.Finder;
import app.process.ProcessBuilder;
import app.process.ProcessManager;
import app.views.AlertLogger;
import app.views.Diag;
import app.views.UserDetailsDialog;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.AppContext.mainController;
import static javafx.collections.FXCollections.observableArrayList;

public class CcbsController {

    @FXML
    private Spinner<Integer> soTbSp;

    @FXML
    private TextField maHD_tf;

    @FXML
    private ProgressBar processBar;

    @FXML
    private Button start_bt;
    @FXML
    private Button choserBt;
    @FXML
    private Button addBt;
    @FXML
    private Button editBt;

    @FXML
    private ComboBox<String> fileTypeCbb;
    @FXML
    private ComboBox<User> fileMauCbb;
    @FXML
    private ComboBox<String> maVungCbb;

    @FXML
    private TableView<Customer> tb_customer;
    @FXML
    private TableColumn<Customer, String> maHDCl;
    @FXML
    private TableColumn<Customer, String> tenCl;
    @FXML
    private TableColumn<Customer, String> gioiTinhCl;
    @FXML
    private TableColumn<Customer, String> ngaySinhCl;
    @FXML
    private TableColumn<Customer, String> soCMCl;
    @FXML
    private TableColumn<Customer, String> ngayCapCl;
    @FXML
    private TableColumn<Customer, String> maTinhCl;
    @FXML
    private TableColumn<Customer, String> maHinhCl;
    @FXML
    private TableColumn<Customer, String> diaChiCl;

    @FXML
    private TableView<Sim> tb_sim;
    @FXML
    private TableColumn<Sim, String> sdtCl;
    @FXML
    private TableColumn<Sim, String> seriCl;
    @FXML
    private TableColumn<Sim, String> otpCl;

    @FXML
    private CheckBox zipCb;
    @FXML
    private CheckBox jpgCb;
    @FXML
    private CheckBox autoCb;
    @FXML
    private CheckBox excelCb;
    @FXML
    private MenuItem themSdtMenu;
    @FXML
    private MenuItem themKhMenu;
    @FXML
    private MenuItem customerDelete;
    @FXML
    private MenuItem customerDeleteAll;
    @FXML
    private MenuItem sdtDelete;
    @FXML
    private MenuItem sdtDeleteAll;

    @FXML
    private Label sumSim;
    @FXML
    private Label sumCustomer;

    private boolean running = false;
    private double done;
    private double max;

    private final AlertLogger logger = AlertLogger.getInstance();
    private final UserDao userDao = UserDao.getInstance();


    public void process() {
        if (running) return;

        if (tb_customer.getItems().size() == 0 || tb_sim.getItems().size() == 0) {
            logger.show(Alert.AlertType.ERROR, "B???n ch??a ch???n file", "H??y ch???n file v?? th??? l???i");
            return;
        }
        if (fileMauCbb.getSelectionModel().getSelectedIndex() == -1) {
            logger.show(Alert.AlertType.ERROR, "B???n ch??a ch???n file m???u", "H??y ch???n file m???u v?? th??? l???i");
            return;
        }
        // CHON FOLDER LUU FILE
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Ch???n n??i l??u");
        File file = chooser.showDialog(Main.stage);
        if (file == null) return;
        String fileParen = file.getPath() + "\\";

        for (int i = 0; i < tb_customer.getItems().size(); i++) tb_customer.getItems().get(i).setMaHD("Ch??? x??? l??");

        ObservableList<Customer> customers = tb_customer.getItems();
        ObservableList<Sim> sims = tb_sim.getItems();

        ProcessBuilder builder = new ProcessBuilder();

        builder.customers(customers)
                .profile(maVungCbb.getSelectionModel().getSelectedItem())
                .sims(sims)
                .maHd(maHD_tf.getText().trim())
                .user(fileMauCbb.getSelectionModel().getSelectedItem())
                .simPerCustomer(soTbSp.getValue())
                .zip(zipCb.isSelected())
                .jpg(jpgCb.isSelected())
                .excel(excelCb.isSelected())
                .auto(autoCb.isSelected())
                .path(fileParen)
                .controller(this);

        ProcessManager manager = new ProcessManager(builder);

        // CHUAN BI CHAY
        running = true;
        start_bt.setText("??ang ch???y");
        processBar.setVisible(true);
        processBar.setProgress(0);
        done = 0;
        max = sims.size();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                manager.run();
                return null;
            }
        };
        task.setOnSucceeded(event1 -> {
            processBar.setProgress(1.0);
            start_bt.setText("B???t ?????u");
            running = false;
            logger.show(Alert.AlertType.INFORMATION, "Xong", checkChuKy(customers));
        });
        task.setOnFailed(evt -> {
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        new Thread(task).start();
    }


    @FXML
    public void initialize() {
        // VALUE BINDING
        maVungCbb.setItems(observableArrayList("HCM", "HYN"));
        maVungCbb.getSelectionModel().select(0);
        fileMauCbb.setItems(observableArrayList(userDao.findByKhuVuc(maVungCbb.getSelectionModel().getSelectedItem())));
        fileTypeCbb.setItems(observableArrayList("S??T-SERI-OTP", "Th??ng tin kh??ch h??ng", "S??T-SERI-OTP-CMND", "SIM-SERI-Th??ng tin kh??ch h??ng"));
        fileTypeCbb.getSelectionModel().select(2);
        soTbSp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 45));
        zipCb.setSelected(true);
        excelCb.setSelected(true);
        autoCb.setSelected(true);

        // CUSTOMER TABLE
        tenCl.setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
        gioiTinhCl.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        ngaySinhCl.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        soCMCl.setCellValueFactory(new PropertyValueFactory<>("soGiayTo"));
        ngayCapCl.setCellValueFactory(new PropertyValueFactory<>("ngayCap"));
        maTinhCl.setCellValueFactory(new PropertyValueFactory<>("maTinh"));
        maHDCl.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        maHDCl.setCellFactory(TextFieldTableCell.forTableColumn());
        maHinhCl.setCellValueFactory(new PropertyValueFactory<>("maHinh"));
        diaChiCl.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        // SIM TABLE
        sdtCl.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        seriCl.setCellValueFactory(new PropertyValueFactory<>("seri"));
        otpCl.setCellValueFactory(new PropertyValueFactory<>("otp"));


        // HANDLE
        themSdtMenu.setOnAction(event -> {
            Diag diag = new Diag("them-sdt");
            AddSimController addSimController = diag.getLoader().getController();
            addSimController.callBack(this);
            diag.show();
        });
        themKhMenu.setOnAction(event -> mainController.danhSachPane.toFront());
        customerDelete.setOnAction(event -> {
            Customer selectedItem = tb_customer.getSelectionModel().getSelectedItem();
            tb_customer.getItems().remove(selectedItem);
        });
        customerDeleteAll.setOnAction(event -> tb_customer.setItems(observableArrayList()));
        sdtDelete.setOnAction(event -> {
            Sim selectedItem = tb_sim.getSelectionModel().getSelectedItem();
            tb_sim.getItems().remove(selectedItem);
        });
        sdtDeleteAll.setOnAction(event -> tb_sim.setItems(observableArrayList()));
        maVungCbb.setOnAction(event ->
                fileMauCbb.setItems(
                        observableArrayList(userDao.findByKhuVuc(maVungCbb.getSelectionModel().getSelectedItem())))
        );
        choserBt.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(Main.stage);
            if (file == null) return;

            int fileType = fileTypeCbb.getSelectionModel().getSelectedIndex();
            switch (fileType) {
                case InputType.CUSTOMER_FILE: {
                    ArrayList<Customer> customers = new CustomerDao(file.getPath()).findAll();
                    tb_customer.getItems().addAll(customers);
                    updateSum();
                    break;
                }
                case InputType.SIM_FILE: {
                    ArrayList<Sim> sims = new SimDao(file.getPath()).findAll();
                    tb_sim.getItems().addAll(sims);
                    updateSum();
                    break;
                }
                case InputType.SIM_SERI_OTP_CMND: {
                    ArrayList<Sim> sims = new SimDao(file.getPath()).findAll();
                    ArrayList<Customer> allCustomers = new CustomerDao().findAll();
                    tb_sim.setItems(observableArrayList(sims));
                    StringBuilder builder = new StringBuilder();
                    List<Customer> customers = sims.stream()
                            .map(ccbsSim -> allCustomers.stream()
                                    .filter(customer -> customer
                                            .getSoGiayTo()
                                            .equals(ccbsSim.getSoGiayTo()))
                                    .findAny().orElseGet(() -> {
                                        builder.append("Kh??ng t??m th???y th??ng tin c???a CMND: '")
                                                .append(ccbsSim.getSoGiayTo()).append("'\n");logger.show(ccbsSim.getSoGiayTo());
                                        return null;
                                    }))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    if(!builder.toString().isBlank()) {
                        logger.show(builder.toString());
                    }
                    tb_customer.setItems(observableArrayList(customers));
                    soTbSp.valueFactoryProperty().get().setValue(1);
                    updateSum();
                    break;
                }
                case InputType.SIM_SERI_OTP_CUSTOMER: {
                    SimCustomerDao simCustomerDao = new SimCustomerDao(file.getPath());
                    ArrayList<Sim> sims = simCustomerDao.findAllSim();
                    ArrayList<Customer> allCustomers = simCustomerDao.findAllCustomer();
                    StringBuilder builder = new StringBuilder();
                    List<Customer> customers = sims.stream()
                            .map(ccbsSim -> allCustomers.stream()
                                    .filter(customer -> customer
                                            .getSoGiayTo()
                                            .equals(ccbsSim.getSoGiayTo()))
                                    .findAny().orElseGet(() -> {
                                        builder.append("Kh??ng t??m th???y th??ng tin c???a CMND: '")
                                                .append(ccbsSim.getSoGiayTo()).append("'\n");
                                        return null;
                                    }))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    if(!builder.toString().isBlank()) {
                        logger.show(builder.toString());
                    }
                    tb_customer.setItems(observableArrayList(customers));
                    tb_sim.setItems(observableArrayList(sims));
                    soTbSp.valueFactoryProperty().get().setValue(1);
                    updateSum();
                    break;
                }
            }
        });

        addBt.setOnAction(event -> {
            UserDetailsDialog dialog = new UserDetailsDialog(new User());
            dialog.showAndWait();
        });

        editBt.setOnAction(event -> {
            User user = fileMauCbb.getSelectionModel().getSelectedItem();
            if (user != null) {
                new UserDetailsDialog(user).showAndWait();
            }
        });

    }

    public void addCustomer(Customer customer) {
        tb_customer.getItems().add(customer);
        updateSum();
    }

    public void addSim(Sim sim) {
        tb_sim.getItems().add(sim);
        updateSum();
    }

    public void updateSum() {
        int sim = tb_sim.getItems().size();
        int customer = tb_customer.getItems().size();
        sumSim.setText(String.valueOf(sim));
        sumCustomer.setText(String.valueOf(customer));
    }

    public void updateTable(Customer customer) {
        ObservableList<Customer> items = tb_customer.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(customer)) items.set(i, customer);
        }
    }

    public void upToProcess() {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                for (int i = 0; i < 100; i++) {
                    countingProcess();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private synchronized void countingProcess() {
        done+= 0.01;
        processBar.setProgress(done / max);
    }

    private String checkChuKy(List<Customer> customers) {
        Finder finder = Finder.getInstance();
        StringBuilder log = new StringBuilder();
        HashSet<Customer> set = new HashSet<>();
        for (Customer customer : customers) {
            if (!finder.haveResult(customer.getSoGiayTo())) {
                set.add(customer);
            }
        }
        for (Customer customer : set) {
            log.append("\n").append(customer.getHoVaTen()).append(" : ").append(customer.getMaHinh());
        }
        if (log.length() > 0) {
           return  "Ch??a c?? ch??? k?? c???a file: " + log;
        }
        return "";
    }

    public void setDone(double done) {
        this.done = done;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
