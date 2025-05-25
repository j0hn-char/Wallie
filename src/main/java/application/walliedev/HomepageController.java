package application.walliedev;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class HomepageController {

    private User user;

    public void setUser(String username){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String getUserInfo = "SELECT * FROM users WHERE username = '" + username + "'";
        
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getUserInfo);

            if (queryResult.next()) {
                user = new User(
                        queryResult.getString("username"),
                        queryResult.getString("password"),
                        queryResult.getString("email")
                );
                System.out.println(queryResult.getString("username") + " " + queryResult.getString("password") + " " + queryResult.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
