package app.views;

import app.Conts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class Diag {
    private Stage stage;
    private FXMLLoader loader;
    public Diag(String fxml){
        try {
            stage = new Stage();
            loader = Conts.openFXML(fxml);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);

            JMetro jMetro = new JMetro(Style.LIGHT);
            jMetro.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Stage getStage() {
        return stage;
    }

    public void show(Window owner){
        stage.initOwner(owner);
        show();
    }
    public void show(){
        stage.show();
    }
}
