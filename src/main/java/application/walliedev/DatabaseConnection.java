package application.walliedev;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        Dotenv dotenv = Dotenv.load();
        String databaseName = dotenv.get("DB_DATABASE");
        String databaseUser = dotenv.get("DB_USERNAME");
        String databasePassword = dotenv.get("DB_PASSWORD");

        String url = "jdbc:mysql://localhost/" + databaseName;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);


        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}
