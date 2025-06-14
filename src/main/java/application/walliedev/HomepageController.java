package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class HomepageController implements Form{

    @FXML
    private VBox paymentListBox, categoriesVBox;

    @FXML
    private Label noBudgetLabel, currencyLabel, balanceLabel, spentLabel, errorLabel, expenseInfoLabel, usernameLabel;

    @FXML
    private MFXButton addBtn, clearBtn;

    @FXML
    private TextField expenseNameTxt, amountTxt;

    @FXML
    private MFXComboBox<String> categoryBox;

    @FXML
    private Rectangle noBudgetBlur, confirmExpenseBlur;

    @FXML
    private AnchorPane confirmExpensePane;
    
    @FXML
    private ProgressBar healthProgressBar, homeProgressBar, leisureProgressBar, shoppingProgressBar, transportProgressBar, otherProgressBar;

    @FXML
    private ImageView logoForAnim;

    private User user;
    private Budget budget;
    private final HashMap<String, Integer> categoryIDList = new HashMap<>();
    private final HashMap<Integer, String> categoryNameList = new HashMap<>();
    private final HashMap<String, String> categoryColorList = new HashMap<>();

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

        categoryColorList.put("Health", "linear-gradient(from 0% 0% to 100% 100%, white, #ffd6de);");    // softer pink
        categoryColorList.put("Home", "linear-gradient(from 0% 0% to 100% 100%, white, #d6ffd0);");      // softer green
        categoryColorList.put("Leisure", "linear-gradient(from 0% 0% to 100% 100%, white, #ffe2c1);");   // softer orange
        categoryColorList.put("Shopping", "linear-gradient(from 0% 0% to 100% 100%, white, #efd6ff);");  // softer purple
        categoryColorList.put("Transport", "linear-gradient(from 0% 0% to 100% 100%, white, #ccd6ff);"); // softer blue
        categoryColorList.put("Other", "linear-gradient(from 0% 0% to 100% 100%, white, #d1d5e6);");     // softer gray-blue

        initializeCategoryComboBox();
        initializeProgressBars();
    }

    private void initializeCategoryComboBox() {
        categoryBox.getItems().addAll(categoryIDList.keySet());
        categoryBox.setMinWidth(150);

    }

    public void initializeProgressBars(){
        healthProgressBar.getStyleClass().add("health-bar");
        homeProgressBar.getStyleClass().add("home-bar");
        leisureProgressBar.getStyleClass().add("leisure-bar");
        shoppingProgressBar.getStyleClass().add("shopping-bar");
        transportProgressBar.getStyleClass().add("transport-bar");
        otherProgressBar.getStyleClass().add("other-bar");
    }

    public void setUser(String username){
        playAnimation();
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
                currencyLabel.setText(getCurrencySymbol());
                usernameLabel.setText(user.getUsername());
                setBudget(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void playAnimation(){
        confirmExpenseBlur.setVisible(true);
        confirmExpenseBlur.setOpacity(1);
        logoForAnim.setVisible(true);

        ScaleTransition fixScale = new ScaleTransition(Duration.seconds(0), logoForAnim);
        fixScale.setFromX(2);
        fixScale.setFromY(2);

        FadeTransition unblurBackground = new FadeTransition(Duration.millis(1500), confirmExpenseBlur);
        unblurBackground.setInterpolator(Interpolator.EASE_BOTH);
        unblurBackground.setFromValue(1);
        unblurBackground.setToValue(0);

        FadeTransition fadeOutLogo = new FadeTransition(Duration.millis(1000), logoForAnim);
        fadeOutLogo.setInterpolator(Interpolator.EASE_BOTH);
        fadeOutLogo.setFromValue(1);
        fadeOutLogo.setToValue(0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        ParallelTransition animationP1 = new ParallelTransition(unblurBackground, fadeOutLogo);
        SequentialTransition animation = new SequentialTransition(fixScale, pause, animationP1);

        animation.setOnFinished(e -> {
            confirmExpenseBlur.setOpacity(0.6);
            confirmExpenseBlur.setVisible(false);
            logoForAnim.setVisible(false);
        });
        animation.play();
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
                balanceLabel.setText(budget.getTotalAmount()-budget.getTotalAmountSpent() + getCurrencySymbol());
                spentLabel.setText(budget.getTotalAmountSpent() + getCurrencySymbol());
                budget.setExpenseHistory();
                setExpenseList();
                initializeNewExpenseBox();
                System.out.println("budget set");
            }else {
                //no budget rules
                addBtn.setDisable(true);
                clearBtn.setDisable(true);
                noBudgetLabel.setVisible(true);
                noBudgetBlur.setVisible(true);
                categoriesVBox.setVisible(false);
                balanceLabel.setText("-");
                spentLabel.setText("-");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void initializeNewExpenseBox(){
        amountTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d*(\\.\\d*)?")){
                return change;
            }
            return null;
        }));
    }
    
    private void setExpenseList() {
        for (Expense expense : budget.getExpenseHistory()) {

            String replaceComma = String.format("%.2f", expense.getAmount());
            replaceComma = replaceComma.replaceAll("," , ".");
            addPaymentRow(
                    expense.getDate(),
                    expense.getName(),
                    replaceComma,
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

        row.setStyle("-fx-padding: 10px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color:" + categoryColorList.get(category) + ";");

        dateLabel.setMaxWidth(170);
        if(nameLabel.getText().length() <= 20) {
            nameLabel.setMaxWidth(170);
        }else{
            nameLabel.setMaxWidth(Double.MAX_VALUE);
        }
        amountLabel.setMaxWidth(Double.MAX_VALUE);

        paymentListBox.getChildren().addFirst(row);
    }

    private void addPaymentRowAndSave(Date date, String name, String amount, String category) {
        addPaymentRow(date, name, amount, category);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO PaymentHistory(budgetId, userId, categoryId, name, amount, paymentDate) VALUES ('";
        String insertValues = budget.getID() + "','" + user.getID() + "','" + categoryIDList.get(category) + "','" + name + "','" + Double.parseDouble(amount) + "','" + date + "')";
        String insertToPaymentList = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();

            statement.executeUpdate(insertToPaymentList);
            System.out.println("Expense added!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void confirmPayment(ActionEvent event) throws IOException {
        if(checkFields()){
            confirmExpensePane.setVisible(true);
            confirmExpenseBlur.setVisible(true);
            System.out.println(confirmExpenseBlur.isVisible());
            expenseInfoLabel.setText("Name: " + expenseNameTxt.getText() + ", Amount: " + amountTxt.getText() + getCurrencySymbol() +", Category: " + categoryBox.getValue());

        }
    }

    public void cancelPayment(ActionEvent event) throws IOException {
        confirmExpenseBlur.setVisible(false);
        confirmExpensePane.setVisible(false);
        clearPayments(event);
        System.out.println("Expense canceled!");
    }

    public void addPayment(ActionEvent event) throws IOException {
        addPaymentRowAndSave(java.sql.Date.valueOf(LocalDate.now()), expenseNameTxt.getText(), amountTxt.getText(), categoryBox.getValue());
        clearPayments(event);
        confirmExpenseBlur.setVisible(false);
        confirmExpensePane.setVisible(false);
        System.out.println("Expense added!");
    }

    public void clearPayments(ActionEvent event) throws IOException {
        expenseNameTxt.setText("");
        amountTxt.setText("");
        categoryBox.setValue(null);
    }

    @Override
    public boolean checkFields() {
        boolean flag = true;
        errorLabel.setVisible(false);
        expenseNameTxt.getStyleClass().remove("error-field");
        amountTxt.getStyleClass().remove("error-field");
        categoryBox.getStyleClass().remove("error-field");

        if(expenseNameTxt.getText().trim().isEmpty()) {
            expenseNameTxt.getStyleClass().add("error-field");
            errorLabel.setVisible(true);
            flag = false;
        }
        if(amountTxt.getText().trim().isEmpty() || !amountTxt.getText().trim().matches("\\d*\\.?\\d+")) {
            amountTxt.getStyleClass().add("error-field");
            errorLabel.setVisible(true);
            flag = false;
        }
        if(categoryBox.getValue() == null){
            categoryBox.getStyleClass().add("error-field");
            errorLabel.setVisible(true);
            flag = false;
        }

        if (flag) {
            return true;
        } else {
            return false;
        }

    }
}
