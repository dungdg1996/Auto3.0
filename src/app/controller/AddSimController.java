package app.controller;

import app.model.Sim;
import app.views.AlertLogger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddSimController {
    private CcbsController ccbsController;
    @FXML private TextField sdt;
    @FXML private TextField seri;
    @FXML private TextField otp;
    @FXML private Button ok;
    private final AlertLogger logger = AlertLogger.getInstance();

    public void callBack(CcbsController ccbsController) {
        this.ccbsController = ccbsController;
    }

    @FXML
    public void initialize() {
        ok.setOnAction(event -> {
            if (sdt.getText().matches("[0-9]{9,11}") &&
                    seri.getText().matches("^(?=[0-9]*$)(?:.{5}|.{10})$") &&
                    otp.getText().matches("[0-9]{5}")) {
                Sim sim = new Sim();
                sim.setSdt(sdt.getText().substring(sdt.getText().length() - 9));
                sim.setSeri(seri.getText());
                sim.setOtp(otp.getText());
                ccbsController.addSim(sim);
            } else logger.show(Alert.AlertType.ERROR, "Bạn nhập sai");
        });
    }

}
