package application.walliedev;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public interface NavBar {
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException;
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException;
    public void switchToBudgetCalc(ActionEvent event, String username);

}
