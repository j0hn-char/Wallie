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

    private MediaPlayer mediaPlayer;

    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeForeground = new FadeTransition(Duration.seconds(2), fade);
        fadeForeground.setFromValue(0);
        fadeForeground.setToValue(1);
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

        URL path = getClass().getResource("/assets/fixedVideo.mp4");
        if (path == null) {
            System.err.println("Video file not found!");
            fadeForeground.play();
            return;
        }

        Media media = new Media(path.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
                mediaView.setPreserveRatio(true);
            }
        });

        mediaPlayer.setOnError(() -> {
            System.err.println("MediaPlayer error: " + mediaPlayer.getError());
            mediaPlayer.stop();
            mediaPlayer.dispose();
            fadeForeground.play();
        });

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
        });

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            pause.play();
        });
    }


    public void switchToLogin(Stage stage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
