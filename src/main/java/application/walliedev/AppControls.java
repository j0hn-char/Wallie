package application.walliedev;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// this interface handles the window's title bar, including the close and minimize buttons.
public interface AppControls {
    public void closeApp();
    public void minimizeApp();
    public void dragWindow(Stage stage);
}
