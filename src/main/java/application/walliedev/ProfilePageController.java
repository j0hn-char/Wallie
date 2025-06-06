package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProfilePageController implements Form{
    @FXML
    private MFXComboBox<String> currencyBox;

    public void initializeCurrencyComboBox() {
        currencyBox.getItems().addAll("€","$");
        currencyBox.setMinWidth(150);
    }
    @Override
    public boolean checkFields() {
        return false;
    }
    //NavBar Interface implementation for Testing
    /*@Override
    public void switchToHomepage(ActionEvent event, String username) {

    }

    @Override
    public void switchToProfile(ActionEvent event, String username) {

    }

    @Override
    public void switchToBudgetCalc(ActionEvent event, String username) {

    }*/
}
