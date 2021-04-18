package app.views;

import app.Conts;
import app.controller.UserController;
import app.model.User;
import app.utils.AppUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDetailsDialog extends Stage {
    public UserDetailsDialog(User user) {
        try {
            FXMLLoader loader = AppUtils.openFXML("user");
            Parent root = loader.load();
            UserController controller = loader.getController();
            controller.setUser(user);
            setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
