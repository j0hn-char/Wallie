package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class RegisterController {
    @FXML
    MFXTextField usernameTxt, pswdTxt, retypeTxt, emailTxt;

    @FXML
    Hyperlink loginLink;
}
