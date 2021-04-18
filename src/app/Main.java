package app;

import app.utils.AppUtils;
import app.utils.ViewUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = AppUtils.openFXML("main");
        Parent root = loader.load();

        Scene scene = new  Scene(root);
        JMetro metro = new JMetro(Style.LIGHT);
        metro.setScene(scene);

        primaryStage.setTitle("Quản lý khách hàng");
        primaryStage.setScene(scene);
        primaryStage.show();

        ViewUtils.enableDragPane(root);
        ViewUtils.alignCenter(primaryStage);
        stage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
