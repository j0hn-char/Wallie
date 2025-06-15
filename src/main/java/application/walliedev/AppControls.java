package application.walliedev;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public interface AppControls {
    public void closeApp();
    public void minimizeApp();
    public void dragWindow(Stage stage);
}
