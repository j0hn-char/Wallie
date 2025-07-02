package application.walliedev;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
/* Wallie is an AI-powered budget calculator and expense manager which enables the user to optimize their finances.
*  The user adds their income, fixed expenses and categories to spend the income and the app creates a budget,
*  based on the user's selections */
public class WallieApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();
        FXMLLoader fxmlLoader = new FXMLLoader(WallieApplication.class.getResource("VideoPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/assets/WallieW.png").toExternalForm()));
        stage.setTitle("Wallie");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}