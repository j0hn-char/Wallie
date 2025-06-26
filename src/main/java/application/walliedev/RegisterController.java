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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.net.URL;

import java.io.IOException;

public class RegisterController implements Form, AppControls{
    @FXML
    private MFXTextField usernameTxt, pswdTxt, retypeTxt, emailTxt;

    @FXML
    private Label errorLabel;

    @FXML
    private MFXProgressSpinner spinner;

    @FXML
    private Rectangle blur;

    @FXML
    private HBox topBar;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private double xOffset = 0;
    private double yOffset = 0;

    public void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        LoginController controller = loader.getController();
        controller.dragWindow(stage);

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
            errorLabel.setText("Please fill out all the fields");
        }
        if(pswdTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            pswdTxt.getStyleClass().add("error-field");
            errorLabel.setText("Please fill out all the fields");
        }
        if(retypeTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            retypeTxt.getStyleClass().add("error-field");
            errorLabel.setText("Please fill out all the fields");
        }
        if(emailTxt.getText().trim().isEmpty())
        {
            fieldsAreOk = false;
            emailTxt.getStyleClass().add("error-field");
            errorLabel.setText("Please fill out all the fields");
        }

        if(!pswdTxt.getText().trim().equals(retypeTxt.getText().trim()))
        {
            pswdTxt.getStyleClass().add("error-field");
            retypeTxt.getStyleClass().add("error-field");
            //errorLabel2.setText("the password fields do not match");
            System.out.println("Passwords do not match!");
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
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String username = usernameTxt.getText().trim();
        String password = pswdTxt.getText();
        String email = emailTxt.getText().trim();
        int preferredCurrency = 1;
        int profileImg = 0;

        String checkIfExistsQuery = "SELECT COUNT(*) FROM Users WHERE username = '" + username + "' OR email = '" + email + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(checkIfExistsQuery);

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                errorLabel.setText("Username or email already exists!");
                return;
            }

            spinner.setVisible(true);
            blur.setVisible(true);

            String insertFields = "INSERT INTO Users(username, password, email, preferredCurrency, profileImg) VALUES ('";
            String insertValues = username + "','" + password + "','" + email + "','" + preferredCurrency + "','" + profileImg + "')";
            String insertToRegister = insertFields + insertValues;

            statement.executeUpdate(insertToRegister);
            System.out.println("User has been created!!");

            Task<Void> emailTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        EmailSender.sendEmail(email, "Welcome to Wallie!", username);
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

    @Override
    public void closeApp() {
        stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeApp() {
        stage = (Stage) usernameTxt.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void dragWindow(Stage stage) {
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        topBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
