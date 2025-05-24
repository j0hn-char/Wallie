module application.walliedev {
    requires MaterialFX;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;


    opens application.walliedev to javafx.fxml;
    exports application.walliedev;
}