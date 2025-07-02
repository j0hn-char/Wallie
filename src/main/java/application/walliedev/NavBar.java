package application.walliedev;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
/* this interface handles the application's navigation bar which contains the button to switch to:
*  -homepage
*  -profile page
*  -budget calculator (Wallie Ai) page */
public interface NavBar {
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException;
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException;
    public void switchToBudgetCalc(MouseEvent event, Parent root, FXMLLoader loader)  throws IOException;

}
