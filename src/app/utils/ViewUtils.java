package app.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewUtils {
    private static double offsetX;
    private static double offsetY;
    private static Stage stage;
    static boolean drag;
    public static void enableDragPane(Parent pane){
        pane.setOnMousePressed(event -> {
            stage = (Stage) pane.getScene().getWindow();
            double startX = event.getScreenX();
            double startY = event.getScreenY();
            double stageX = stage.getX();
            double stageY = stage.getY();
            offsetX = stageX - startX;
            offsetY = stageY - startY;
            drag = true;
        });
        pane.setOnMouseDragged(event -> {
            if(drag){
                double endX = event.getScreenX();
                double endY = event.getScreenY();
                stage.setX( endX + offsetX);
                stage.setY( endY + offsetY);
            }
        });
        pane.setOnMouseReleased(event -> {
            drag = false;
            offsetY = 0;
            offsetX = 0;
        });
    }
    public static void alignCenter(Stage stage){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
