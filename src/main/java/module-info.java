module application.walliedev {
    requires MaterialFX;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires javafx.media;


    opens application.walliedev to javafx.fxml;
    exports application.walliedev;
}