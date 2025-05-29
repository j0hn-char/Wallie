package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class HomepageController {

    @FXML
    private VBox paymentListBox, categoriesVBox;

    @FXML
    private Label noBudgetLabel;

    @FXML
    private MFXButton addBtn, clearBtn;

    @FXML
    private TextField expenseNameTxt, amountTxt;

    @FXML
    private MFXComboBox<String> categoryBox;

    private User user;
    private Budget budget;
    private HashMap<String, Integer> categoryIDList = new HashMap<>();
    private HashMap<Integer, String> categoryNameList = new HashMap<>();

    public void initializeCategoryLists() {
        categoryNameList.put(1, "Health");
        categoryNameList.put(2, "Home");
        categoryNameList.put(3, "Leisure");
        categoryNameList.put(4, "Shopping");
        categoryNameList.put(5, "Transport");
        categoryNameList.put(6, "Other");

        categoryIDList.put("Health", 1);
        categoryIDList.put("Home", 2);
        categoryIDList.put("Leisure", 3);
        categoryIDList.put("Shopping", 4);
        categoryIDList.put("Transport", 5);
        categoryIDList.put("Other", 6);

        initializeCategoryComboBox();
    }

    private void initializeCategoryComboBox() {
        categoryBox.getItems().addAll(categoryIDList.keySet());
        categoryBox.setMinWidth(150);
    }

    public void setUser(String username){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String getUserInfo = "SELECT * FROM users WHERE username = '" + username + "'";
        
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getUserInfo);

            if (queryResult.next()) {
                user = new User(
                        queryResult.getInt("userID"),
                        queryResult.getString("username"),
                        queryResult.getString("password"),
                        queryResult.getString("email"),
                        queryResult.getInt("preferredCurrency"),
                        queryResult.getInt("profileImg")
                );
                System.out.println(queryResult.getString("username") + " " + queryResult.getString("password") + " " + queryResult.getString("email"));
                setBudget(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    
    public void setBudget(User user) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        
        String getBudgetInfo = "SELECT * FROM budgets WHERE userId = '" + user.getID() + "'";
        
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getBudgetInfo);
            
            if (queryResult.next()) {
                budget = new Budget(
                        queryResult.getInt("budgetId"),
                        queryResult.getDouble("totalAmount"),
                        queryResult.getDouble("totalAmountSpent")
                );
                budget.setExpenseHistory();
                setExpenseList();
                System.out.println("budget set");
            }else {
                //no budget rules
                addBtn.setDisable(true);
                clearBtn.setDisable(true);
                noBudgetLabel.setVisible(true);
                categoriesVBox.setVisible(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    
    private void setExpenseList() {
        for (Expense expense : budget.getExpenseHistory()) {
            addPaymentRow(
                    expense.getDate(),
                    expense.getName(),
                    String.format("%.2f", expense.getAmount()),
                    categoryNameList.get(expense.getCategoryId())
            );
        }
    }

    private String getCurrencySymbol() {
        return switch (user.getCurrency()) {
            case 1 -> "€";
            case 2 -> "$";
            case 3 -> "£";
            default -> "€";
        };
    }

    private void addPaymentRow(Date date, String name, String amount, String category) {
        Label dateLabel = new Label(date.toString());
        dateLabel.setFont(new Font("Segoe UI Semibold", 15));
        dateLabel.setStyle("-fx-text-fill: #3700b3;");

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font("Segoe UI Semibold", 15));
        nameLabel.setStyle("-fx-text-fill: #3700b3;");

        Label amountLabel = new Label(getCurrencySymbol() + amount);
        amountLabel.setFont(new Font("Segoe UI Semibold", 15));
        amountLabel.setStyle("-fx-text-fill: #3700b3;");

        Label categoryLabel = new Label(category);
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

//        System.out.println(categoryIDList.get(category));

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO PaymentHistory(budgetId, userId, categoryId, name, amount, paymentDate) VALUES ('";
        String insertValues = budget.getID() + "','" + user.getID() + "','" + categoryIDList.get(category) + "','" + name + "','" + Double.parseDouble(amount) + "','" + date + "')";
        String insertToPaymentList = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();

            statement.executeUpdate(insertToPaymentList);
            System.out.println("Expense added!");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addPayment(ActionEvent event) throws IOException {
        addPaymentRow(java.sql.Date.valueOf(LocalDate.now()), expenseNameTxt.getText(), amountTxt.getText(), categoryBox.getValue());
    }
}
