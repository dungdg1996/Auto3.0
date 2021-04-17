package app.controller;

import app.dao.CustomerDao;
import app.model.Customer;
import app.utils.StringUtils;
import app.views.Diag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;


public class DanhSachController{
    @FXML TableView<Customer> tb_kh;
    @FXML private TableColumn<Customer, Boolean> cl_select;
    @FXML private TableColumn<Customer, String> cl_maHinh;
    @FXML private TableColumn<Customer, String> cl_ten;
    @FXML private TableColumn<Customer, String> cl_gioiTinh;
    @FXML private TableColumn<Customer, String> clNgaySinh;
    @FXML private TableColumn<Customer, String> cl_loaiGiayTo;
    @FXML private TableColumn<Customer, String> cl_soCM;
    @FXML private TableColumn<Customer, String> cl_ngayCap;
    @FXML private TableColumn<Customer, String> cl_noiCap;
    @FXML private TableColumn<Customer, String> cl_diaChi;

    @FXML private Button btn_delete;
    @FXML private Button btn_add;

    @FXML private TextField tf_seach;

    @FXML private MenuItem editMenu;
    @FXML private MenuItem addCcbsMenu;
    @FXML private MenuItem menu_export1;
    @FXML private MenuItem selectAllMenu;
    @FXML private MenuItem unSelectAllMenu;
    @FXML private MenuItem deleteMenu;

    @FXML private ProgressBar progressBar;

    @FXML private Label sumLb;


    private FilteredList<Customer> filteredList ;
    private ObservableList khList;
    private CustomerDao dao;
    private MainController mainController;
    private CcbsController ccbsController;
    private int sum = 0;

    private void setHandle(){
        editMenu.setOnAction(event -> {
            Customer selectedItem = tb_kh.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Diag diag = new Diag("add");
                CreateCustomerController createCustomerController = diag.getLoader().getController();
                createCustomerController.init(selectedItem);
                diag.show();
            }
        });
        btn_delete.setOnAction(event -> {
            ObservableList<Customer> selectedRows = tb_kh.getSelectionModel().getSelectedItems();
            if (selectedRows.size() > 0) {
                //delete (chưa viết)
            }
        });

        btn_add.setOnAction(event -> {
            ObservableList<Customer> customers = tb_kh.getItems();
            customers.stream()
                    .filter(Customer::isSelected)
                    .forEach(customer -> ccbsController.addCustomer(customer));
            mainController.ccbsPane.toFront();
        });

        addCcbsMenu.setOnAction(event -> {
            Customer selectedItem = tb_kh.getSelectionModel().getSelectedItem();
            ccbsController.addCustomer(selectedItem);
        });

        selectAllMenu.setOnAction(event -> {
            tb_kh.getItems().forEach(customer -> customer.setSelected(true));
        });
        unSelectAllMenu.setOnAction(event -> {
            tb_kh.getItems().forEach(customer -> customer.setSelected(false));
        });


        //chung nang tim kiem
        tf_seach.setOnAction(event -> {
            String[] values = (StringUtils.unAccent(tf_seach.getText().toUpperCase())+" ").split(" ");
            filteredList.setPredicate(
                    record -> {
                        String key = StringUtils.unAccent(record.toString()).toUpperCase();
                        for (String value : values)
                            if (!key.contains(value)) return false;
                        return true; // or true
                    }
            );
            tb_kh.setItems(filteredList);
        });
    }

    private void updateSum(){
        int sum = (int) tb_kh.getItems().stream().filter(Customer::isSelected).count();
        sumLb.setText(String.valueOf(sum));
    }

    private void tableBinding(){
        //selected column
        cl_select.setCellValueFactory(param -> param.getValue().getSelected());
        cl_select.setCellFactory(p -> {
            CheckBoxTableCell<Customer, Boolean> cell = new CheckBoxTableCell<>(){
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    updateSum();
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        //double click to chose
        tb_kh.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tb_kh.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Customer rowData = row.getItem();
                    if (rowData.getSelected().getValue()) {
                        rowData.setSelected(false);
                    } else {
                        rowData.setSelected(true);
                    }
                }
            });
            return row;
        });
        cl_maHinh.setCellValueFactory(new PropertyValueFactory<>("maHinh"));
        cl_ten.setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
        cl_gioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        clNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        cl_loaiGiayTo.setCellValueFactory(new PropertyValueFactory<>("loaiGiayTo"));
        cl_soCM.setCellValueFactory(new PropertyValueFactory<>("soGiayTo"));
        cl_ngayCap.setCellValueFactory(new PropertyValueFactory<>("ngayCap"));
        cl_noiCap.setCellValueFactory(new PropertyValueFactory<>("maTinh"));
        cl_diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

    }

    public void loadData(){
        progressBar.setVisible(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                dao = new CustomerDao();
                khList = FXCollections.observableArrayList(dao.findAll());
                filteredList = new FilteredList<>(khList);
                tb_kh.setItems(filteredList);
                tb_kh.setEditable(true);
                return null;
            }
        };
        task.setOnSucceeded(event -> progressBar.setVisible(false));
        new Thread(task).start();
    }

    @FXML
    public void initialize() {
        tableBinding();
        setHandle();
        loadData();
    }

    public void callBack(MainController mainController) {
        this.mainController = mainController;
    }

    public void callBack(CcbsController ccbsController) {
        this.ccbsController = ccbsController;
    }
}
