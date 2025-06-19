package application.walliedev;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IntroVideoController implements Initializable {

    @FXML
    private Rectangle fade;

    @FXML
    private MediaView mediaView;

    private Scene scene;
    private Parent root;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeForeground = new FadeTransition(Duration.seconds(2), fade);
        fadeForeground.setFromValue(0);
        fadeForeground.setToValue(1);

        URL resource = getClass().getResource("/assets/WelcomeChime.mp3"); // adjust path
        if (resource != null) {
            Media sound = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(sound);
        } else {
            System.out.println("Audio file not found");
        }

        fadeForeground.setOnFinished(e -> {
            try {
                Stage stage = (Stage) mediaView.getScene().getWindow();
                switchToLogin(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(e -> fadeForeground.play());

        PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
        pause2.setOnFinished(e -> mediaPlayer.play());

        mediaView.setMediaPlayer(mediaPlayer);
        pause2.play();
        pause.play();
    }


    public void switchToLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        root = loader.load();

        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        LoginController controller = loader.getController();
        controller.dragWindow(stage);

        stage.setScene(scene);
        stage.show();
    }
}
