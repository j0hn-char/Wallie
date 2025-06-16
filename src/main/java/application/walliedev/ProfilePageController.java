package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfilePageController implements Form, NavBar, AppControls{
    @FXML
    private MFXComboBox<String> currencyBox;
    @FXML
    private Label displayUsernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private MFXTextField currPswdTxt, newPswdTxt, retypeTxt;
    @FXML
    private Label errorLabel;
    @FXML
    private Rectangle focusGradient, whiteOut;
    @FXML
    private ImageView homePageLogo, goToProfileBtn;
    @FXML
    private MFXButton wallieAiBtn;
    @FXML
    private HBox topBar;

    private User user;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private double xOffset = 0;
    private double yOffset = 0;
    private final DoubleProperty focusDistance = new SimpleDoubleProperty(0);

    public void initializeCurrencyComboBox() {
        currencyBox.getItems().addAll("€","$");
        currencyBox.setMinWidth(150);
    }

    public void setUser(User user) {
        this.user=user;

        displayUsernameLabel.setText(user.getUsername());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
    }
    public boolean checkFields() {
        String currPswd = currPswdTxt.getText();
        String newPswd = newPswdTxt.getText();
        String retypePswd = retypeTxt.getText();

        boolean fieldsAreOk = true;
        errorLabel.setText("");
        currPswdTxt.getStyleClass().remove("error-field");
        newPswdTxt.getStyleClass().remove("error-field");
        retypeTxt.getStyleClass().remove("error-field");

        if(currPswd.isEmpty())
        {
            fieldsAreOk = false;
            currPswdTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }
        if(newPswd.isEmpty())
        {
            fieldsAreOk = false;
            newPswdTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }
        if(retypePswd.isEmpty())
        {
            fieldsAreOk = false;
            retypeTxt.getStyleClass().add("error-field");
            errorLabel.setText("please fill out all the fields");
        }

        if(!newPswd.equals(retypePswd) && !newPswd.isEmpty() && !retypePswd.isEmpty())
        {
            newPswdTxt.getStyleClass().add("error-field");
            retypeTxt.getStyleClass().add("error-field");
            //errorLabel2.setText("the password fields do not match");
            errorLabel.setText("Passwords do not match");
            System.out.println("Passwords do not match!");
            fieldsAreOk = false;
        }
        else
            System.out.println("passwords match");

        return fieldsAreOk;
    }

    public void saveChangesBtnPressed(){
        if(checkFields()){
           updatePassword();
        }
    }

   public void changeCurrency(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String selectedCurrency = currencyBox.getValue();
        int newCurr = 0;
        if(selectedCurrency.equals("€"))
            newCurr = 1;
        else if(selectedCurrency.equals("$"))
            newCurr = 2;

       Statement statement = null;
        if(newCurr!=user.getCurrency() && newCurr>0){
            try {
                statement = connectDB.createStatement();
                statement.executeUpdate("UPDATE Users SET preferredCurrency = '" +newCurr + "' WHERE username = '" + user.getUsername() + "';");
                user.setCurrency(newCurr);
                System.out.println("Changed Currency");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updatePassword(){
        String currPswd = currPswdTxt.getText();
        String newPswd = newPswdTxt.getText();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        Statement statement = null;

        try {
            statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery("SELECT password FROM Users WHERE username = '"+user.getUsername()+"';");
            if (rs.next()) {
                String dbPassword = rs.getString("password");

                if (currPswd.equals(dbPassword)) {
                    statement.executeUpdate("UPDATE Users SET password = '" + newPswd + "' WHERE username = '" + user.getUsername() + "';");
                } else {
                    currPswdTxt.getStyleClass().add("error-field");
                    errorLabel.setText("Incorrect Current Password");
                }
            } else {
                errorLabel.setText("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteAccount(ActionEvent event) throws IOException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        Statement statement = null;
        try {
            statement = connectDB.createStatement();
            statement.executeUpdate("DELETE FROM Users WHERE username = '"+user.getUsername()+"';");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logOut(event);
    }
    public void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void homepageNavAnimationIn(){
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(266);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(1);
        whiteOutAnim.setToValue(0);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void homepageNavAnimationOut(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
        root = loader.load();

        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH))
        );

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(-266);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
            try {
                switchToHomepage(event, user.getUsername(), root, loader);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        anim.play();
    }

    private void updateGradient(double focusDistance) {
        focusGradient.setFill(new RadialGradient(
                0,
                focusDistance,
                0.5, 0.5,
                0.48,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#b787ff")),
                new Stop(1, Color.TRANSPARENT)
        ));
    }

    @Override
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException {
        HomepageController controller = loader.getController();
        controller.initializeCategoryLists();
        controller.setUser(username);
        controller.profileNavAnimationIn();

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @Override
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {}

    public void switchToBudgetCalc(ActionEvent event, String username) {

    }

    @Override
    public void closeApp() {
        stage = (Stage) currPswdTxt.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeApp() {
        stage = (Stage) currPswdTxt.getScene().getWindow();
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
