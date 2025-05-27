package application.walliedev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class HomepageController {

    @FXML
    private VBox paymentListBox;

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

    public void addPayment(ActionEvent event) throws IOException {
        Label dateLabel = new Label("2025-05-25");
        dateLabel.setFont(new Font("Segoe UI Semibold", 15));
        dateLabel.setStyle("-fx-text-fill: #3700b3;");

        Label nameLabel = new Label("Spotify");
        nameLabel.setFont(new Font("Segoe UI Semibold", 15));
        nameLabel.setStyle("-fx-text-fill: #3700b3;");

        Label amountLabel = new Label("€9.99");
        amountLabel.setFont(new Font("Segoe UI Semibold", 15));
        amountLabel.setStyle("-fx-text-fill: #3700b3;");

        Label categoryLabel = new Label("Subscription");
        categoryLabel.setFont(new Font("Segoe UI Semibold", 15));
        categoryLabel.setStyle("-fx-text-fill: #3700b3;");

        HBox row = new HBox(dateLabel, nameLabel, amountLabel, categoryLabel);
        HBox.setHgrow(dateLabel, Priority.ALWAYS);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        HBox.setHgrow(amountLabel, Priority.ALWAYS);
        HBox.setHgrow(categoryLabel, Priority.ALWAYS);

        dateLabel.setMaxWidth(170);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        amountLabel.setMaxWidth(Double.MAX_VALUE);

        Separator separator = new Separator();

        paymentListBox.getChildren().addFirst(separator);
        paymentListBox.getChildren().addFirst(row);
    }
}
