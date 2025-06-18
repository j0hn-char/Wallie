module application.walliedev {
    requires MaterialFX;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires javafx.media;
    requires java.net.http;
    requires com.google.gson;
    requires io.github.cdimascio.dotenv.java;


    opens application.walliedev to javafx.fxml;
    exports application.walliedev;
}