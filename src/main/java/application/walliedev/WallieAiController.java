package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

public class WallieAiController implements NavBar, AppControls{

    @FXML
    private HBox topBar;

    @FXML
    private MFXButton wallieAiBtn;

    @FXML
    private ImageView homePageLogo, goToProfileBtn;

    @FXML
    private Rectangle focusGradient, whiteOut;

    @FXML
    private Label usernameLabel;

    private User user;
    private Stage stage;
    private Parent root;
    private double xOffset = 0;
    private double yOffset = 0;
    private final DoubleProperty focusDistance = new SimpleDoubleProperty(0);

    public void setUser(User user) {
        this.user=user;
        usernameLabel.setText(user.getUsername());
    }

    @Override
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        HomepageController controller = loader.getController();
        controller.initializeCategoryLists();
        controller.setUser(username);
        controller.dragWindow(stage);
        controller.wallieAiNavAnimationIn(event);

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @Override
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        ProfilePageController controller = loader.getController();
        controller.initializeCurrencyComboBox();
        controller.setUser(user);
        controller.dragWindow(stage);
        controller.wallieAiNavAnimationIn();

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @Override
    public void switchToBudgetCalc(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {

    }

    public void homepageNavAnimationIn(MouseEvent event) throws IOException {
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(-266);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void homepageNavAnimationOut(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
        Parent newRoot = loader.load();

        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 1.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(266);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
            try {
                switchToHomepage(event, user.getUsername(), newRoot, loader);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        anim.play();
    }

    public void profileNavAnimationIn(MouseEvent event) throws IOException {
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusGradient.setTranslateX(focusGradient.getTranslateX() + 266);

        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(1.4), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(-532);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(1.4), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void profileNavAnimationOut(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
        Parent newRoot = loader.load();

        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 1.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(1.4), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(1.4), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(532);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
            try {
                switchToProfile(event, newRoot, loader);
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
    public void closeApp() {
        stage = (Stage) topBar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeApp() {
        stage = (Stage) topBar.getScene().getWindow();
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