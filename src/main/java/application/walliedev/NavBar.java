package application.walliedev;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public interface NavBar {
    public void switchToHomepage(ActionEvent event, String username);
    public void switchToProfile(MouseEvent event, String username) throws IOException;
    public void switchToBudgetCalc(ActionEvent event, String username);

}
