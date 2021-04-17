package app.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertLogger {
    public ButtonType show(Alert.AlertType type, String head, String context) {
        Alert alert = new Alert(type, context);
        alert.setHeaderText(head);
        return alert.showAndWait().orElse(null);
    }

    public ButtonType show(Alert.AlertType type, String head) {
        return show(type, head, "");
    }

    public ButtonType show(String head) {
        return show(Alert.AlertType.INFORMATION, head, "");
    }

    public ButtonType show(String head, String context) {
        return show(Alert.AlertType.INFORMATION, head, context);
    }

    public static AlertLogger getInstance(){
        return SingletonAlertLogger.INSTANCE;
    }

    private static class SingletonAlertLogger{
        static final AlertLogger INSTANCE = new AlertLogger();
    }

}

