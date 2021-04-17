package app;

import app.controller.CreateCustomerController;
import app.controller.CcbsController;
import app.controller.DanhSachController;
import app.controller.MainController;

public class AppContext {
    public static MainController mainController;
    public static CcbsController ccbsController;
    public static CreateCustomerController createCustomerController;
    public static DanhSachController danhSachController;

    public static void init() {
        danhSachController.callBack(mainController);
        danhSachController.callBack(ccbsController);
    }
}
