package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import java.io.File;
import java.net.URL;

import java.io.IOException;

public class RegisterController {
    @FXML
    MFXTextField usernameTxt, pswdTxt, retypeTxt, emailTxt;

    @FXML
    Label errorLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public boolean checkFields() {

        boolean fieldsAreOk = true;
        errorLabel.setText("");
        usernameTxt.getStyleClass().remove("error-field");
        pswdTxt.getStyleClass().remove("error-field");
        retypeTxt.getStyleClass().remove("error-field");
        emailTxt.getStyleClass().remove("error-field");

        if(usernameTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            usernameTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }
        if(pswdTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            pswdTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }
        if(retypeTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            retypeTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }
        if(emailTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            emailTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }

        if(!pswdTxt.getText().trim().equals(retypeTxt.getText().trim()))
        {
            pswdTxt.getStyleClass().add("error-field");
            retypeTxt.getStyleClass().add("error-field");
            //errorLabel2.setText("the password fields dont match");
            System.out.println("Passwords do not match!");
            fieldsAreOk = false;
        }

        return fieldsAreOk;
    }

    public void registerButtonPressed(ActionEvent event){
        if(checkFields()) {
            registerUser();
        }
    }

    public void registerUser(){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String username = usernameTxt.getText().trim();
        String password = pswdTxt.getText();
        String email = emailTxt.getText().trim();
        int preferredCurrency = 1;
        int profileImg = 1;

        String insertFields = "";
        String insertValues = "";
        String insertToRegister = insertFields + insertValues;
    }
}
