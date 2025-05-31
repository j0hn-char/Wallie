package application.walliedev;

import javafx.event.ActionEvent;

public interface NavBar {
    public void switchToHomepage(ActionEvent event, String username);
    public void switchToProfile(ActionEvent event, String username);
    public void switchToBudgetCalc(ActionEvent event, String username);

}
