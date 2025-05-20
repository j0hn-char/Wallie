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

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.net.URL;

import java.io.IOException;

public class LoginController {
    @FXML
    MFXTextField usernameTxt, pswdTxt;

    @FXML
    Label errorLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public boolean checkFields(){
        errorLabel.setText("");
        usernameTxt.getStyleClass().remove("error-field");
        pswdTxt.getStyleClass().remove("error-field");

        if(usernameTxt.getText().trim().isEmpty())
        {
            usernameTxt.getStyleClass().add("error-field");
            errorLabel.setText("Missing information");
        }
        if(pswdTxt.getText().trim().isEmpty())
        {
            pswdTxt.getStyleClass().add("error-field");
            errorLabel.setText("Missing information");
        }

        if ((!usernameTxt.getText().trim().isEmpty() && (!pswdTxt.getText().trim().isEmpty()))) {
            return true;
        } else {
            return false;
        }

    }

    public void loginButtonPressed(ActionEvent event) {
        if (checkFields()){
            validateLogin();
        }
    }

    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM users WHERE username = '" + usernameTxt.getText() + "' AND password = '" + pswdTxt.getText() + "'";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1) {
                    System.out.println("You logged IN!!!");
                } else {
                    System.out.println("Wrong Credentials!");
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}
