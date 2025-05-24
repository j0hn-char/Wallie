package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.net.URL;

import java.io.IOException;

public class RegisterController {
    @FXML
    MFXTextField usernameTxt, pswdTxt, retypeTxt, emailTxt;

    @FXML
    Label errorLabel;

    @FXML
    MFXProgressSpinner spinner;

    @FXML
    Rectangle blur;

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
        }else if(!emailTxt.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            emailTxt.getStyleClass().add("error-field");
            errorLabel.setText("Please enter a valid email address");
            fieldsAreOk = false;
        }

        if(!pswdTxt.getText().trim().equals(retypeTxt.getText().trim()))
        {
            pswdTxt.getStyleClass().add("error-field");
            retypeTxt.getStyleClass().add("error-field");
            errorLabel.setText("the password fields do not match");
//            System.out.println("Passwords do not match!");
            fieldsAreOk = false;
        }

        return fieldsAreOk;
    }

    public void registerButtonPressed(ActionEvent event){
        if(checkFields()) {
            registerUser(event);
        }
    }

    public void registerUser(ActionEvent event) {
        spinner.setVisible(true);
        blur.setVisible(true);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String username = usernameTxt.getText().trim();
        String password = pswdTxt.getText();
        String email = emailTxt.getText().trim();
        int preferredCurrency = 1;
        int profileImg = 1;

        String checkIfExistsQuery = "SELECT COUNT(*) FROM Users WHERE username = '" + username + "' OR email = '" + email + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(checkIfExistsQuery);

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Username or email already exists!");
                return;
            }

            String insertFields = "INSERT INTO Users(username, password, email, preferredCurrency, profileImg) VALUES ('";
            String insertValues = username + "','" + password + "','" + email + "','" + preferredCurrency + "','" + profileImg + "')";
            String insertToRegister = insertFields + insertValues;

            statement.executeUpdate(insertToRegister);
            System.out.println("User has been created!!");

            Task<Void> emailTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        EmailSender.sendEmail(email, "Welcome to Wallie!", "Hello " + username + ", you have successfully registered to Wallie");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            emailTask.setOnSucceeded(e -> {
                spinner.setVisible(false);
                blur.setVisible(false);
                System.out.println("Email sent successfully");
                try {
                    switchToLogin(event);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            emailTask.setOnFailed(e -> {
                spinner.setVisible(false);
                blur.setVisible(false);
                System.out.println("Email failed to send");
            });

            new Thread(emailTask).start();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}