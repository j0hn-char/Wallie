package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfilePageController implements Form{
    @FXML
    private MFXComboBox<String> currencyBox;
    @FXML
    private Label displayUsernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;


    private User user;

    public void initializeCurrencyComboBox() {
        currencyBox.getItems().addAll("€","$");
        currencyBox.setMinWidth(150);
    }
    @Override
    public boolean checkFields() {
        return false;
    }

    public void setUser(User user) {
        this.user=user;

        displayUsernameLabel.setText(user.getUsername());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
//        currencyLabel.setText(getCurrencySymbol());
//        usernameLabel.setText(user.getUsername());
//        setBudget(user);
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
