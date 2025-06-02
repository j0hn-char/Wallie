package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.IOException;

public class LoginController implements Form{
    @FXML
    private MFXTextField usernameTxt, pswdTxt;

    @FXML
    private Label errorLabel;

    @FXML
    private MFXProgressSpinner spinner;

    @FXML
    private Rectangle blur;

    @FXML
    private ImageView logoForAnim;

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

    private void switchToHomepage(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
        root = loader.load();

        HomepageController controller = loader.getController();
        controller.initializeCategoryLists();
        controller.setUser(username);

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
            validateLogin(event);
        }
    }

    public void validateLogin(ActionEvent event){
        errorLabel.setText("");
        spinner.setVisible(true);
        blur.setVisible(true);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM users WHERE username = '" + usernameTxt.getText().trim() + "' AND password = '" + pswdTxt.getText() + "'";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1) {
                    System.out.println("successful login");

                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
//                    pause.setOnFinished(e -> spinner.setVisible(false));

                    FadeTransition fadeSpinner = new FadeTransition(Duration.millis(1000), spinner);
                    fadeSpinner.setInterpolator(Interpolator.EASE_BOTH);
                    fadeSpinner.setFromValue(1);
                    fadeSpinner.setToValue(0);

                    FadeTransition blurBackground = new FadeTransition(Duration.millis(1000), blur);
                    blurBackground.setInterpolator(Interpolator.EASE_BOTH);
                    blurBackground.setFromValue(0.5);
                    blurBackground.setToValue(1);

                    ScaleTransition enlargeLogo = new ScaleTransition(Duration.millis(1500), logoForAnim);
                    enlargeLogo.setInterpolator(Interpolator.EASE_BOTH);
                    enlargeLogo.setFromX(1);
                    enlargeLogo.setFromY(1);
                    enlargeLogo.setToX(2);
                    enlargeLogo.setToY(2);

                    TranslateTransition moveLogo = new TranslateTransition(Duration.millis(1500), logoForAnim);
                    moveLogo.setInterpolator(Interpolator.EASE_BOTH);
                    moveLogo.setFromX(0);
                    moveLogo.setFromY(0);
                    moveLogo.setToX(0);
                    moveLogo.setToY(-550);

                    ParallelTransition animationP1 = new ParallelTransition(fadeSpinner, blurBackground, enlargeLogo, moveLogo);
                    SequentialTransition animation = new SequentialTransition(pause, animationP1, new PauseTransition(Duration.seconds(1)) );
                    animation.setOnFinished(e -> {
                        try {
                            switchToHomepage(event, usernameTxt.getText());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    animation.play();
                } else {
                    spinner.setVisible(false);
                    blur.setVisible(false);
                    errorLabel.setText("Incorrect password or username");
                }
            }
//            spinner.setVisible(false);
//            blur.setVisible(false);

        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}
