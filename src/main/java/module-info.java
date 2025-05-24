module application.walliedev {
    requires MaterialFX;
    requires java.sql;
    requires mysql.connector.j;


    opens application.walliedev to javafx.fxml;
    exports application.walliedev;
}