package app.controller;

import app.AppContext;
import app.dao.CustomerDao;
import app.views.Site;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class MainController {

    public static CustomerDao customerDao;
    public Parent ccbsPane;
    public Parent danhSachPane;

    @FXML
    private HBox pane_khachHang;

    @FXML
    private HBox pane_smcs;

    @FXML
    private HBox pane_ccbs;

    @FXML
    private HBox pane_old;

    @FXML
    private StackPane pane_stack;

    @FXML
    private HBox pane_nhapTay;


    private void loadSite(){

        Site add = new Site("add");
        Site list = new Site("danhsach");
        Site ccbs = new Site("ccbs");
        pane_stack.getChildren()
                .addAll(
                        ccbs.getRoot(),
                        add.getRoot(),
                        list.getRoot()
                );
        pane_ccbs.setOnMouseClicked(event -> ccbs.getRoot().toFront());
        pane_khachHang.setOnMouseClicked(event -> {
            list.getRoot().toFront();
        });
        pane_nhapTay.setOnMouseClicked(event -> add.getRoot().toFront());

        ccbsPane = ccbs.getRoot();
        danhSachPane = list.getRoot();

        AppContext.mainController = this;
        AppContext.createCustomerController = add.getLoader().getController();
        AppContext.danhSachController = list.getLoader().getController();
        AppContext.ccbsController = ccbs.getLoader().getController();
        AppContext.init();
    }

    private void loadData(){
        customerDao = new CustomerDao();
    }

    @FXML
    public void initialize() {
        //load pane
        loadSite();
        loadData();

    }

}
