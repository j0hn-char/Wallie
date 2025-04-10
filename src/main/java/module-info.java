module application.walliedev {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.walliedev to javafx.fxml;
    exports application.walliedev;
}