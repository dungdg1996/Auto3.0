package app.views;

import app.Conts;
import app.utils.AppUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Site {
    private FXMLLoader loader;
    private Parent root;
    public Site(String sour){
        loader = AppUtils.openFXML(sour);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Parent getRoot() {
        return root;
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
