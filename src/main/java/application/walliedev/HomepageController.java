package application.walliedev;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class HomepageController implements Form, NavBar, AppControls{
    @FXML
    private VBox paymentListBox;

    @FXML
    private Label noBudgetLabel, currencyLabel, balanceLabel, spentLabel, errorLabel, expenseInfoLabel, usernameLabel, noExpenseLabel;

    @FXML
    private MFXButton addBtn, clearBtn, wallieAiBtn;

    @FXML
    private TextField expenseNameTxt, amountTxt;

    @FXML
    private MFXComboBox<String> categoryBox;

    @FXML
    private Rectangle noBudgetBlur, confirmExpenseBlur, focusGradient, whiteOut, pieChartBlur;

    @FXML
    private AnchorPane confirmExpensePane;

    @FXML
    private ImageView logoForAnim, goToProfileBtn, homePageLogo;

    @FXML
    private HBox topBar;

    @FXML
    private PieChart pieChart;

    private Stage stage;

    private User user;
    private Budget budget;
    private double xOffset = 0;
    private double yOffset = 0;
    private final DoubleProperty focusDistance = new SimpleDoubleProperty(0);
    private final HashMap<String, Integer> categoryIDList = new HashMap<>();
    private final HashMap<Integer, String> categoryNameList = new HashMap<>();
    private final HashMap<String, String> categoryColorList = new HashMap<>();
    private final HashMap<String, String> categoryChartColorList = new HashMap<>();
    private Image profileImage;

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

        categoryColorList.put("Health", "linear-gradient(from 0% 0% to 100% 100%, white, #f9d7c4);");
        categoryColorList.put("Home", "linear-gradient(from 0% 0% to 100% 100%, white, #ffe3a7);");
        categoryColorList.put("Leisure", "linear-gradient(from 0% 0% to 100% 100%, white, #d1e6d1);");
        categoryColorList.put("Shopping", "linear-gradient(from 0% 0% to 100% 100%, white, #bbd4df);");
        categoryColorList.put("Transport", "linear-gradient(from 0% 0% to 100% 100%, white, #b4b8e1);");
        categoryColorList.put("Other", "linear-gradient(from 0% 0% to 100% 100%, white, #dbc3e7);");

        categoryChartColorList.put("Health", "#e46537");
        categoryChartColorList.put("Home", "#f0a834");
        categoryChartColorList.put("Leisure", "#61b661");
        categoryChartColorList.put("Shopping", "#51acc7");
        categoryChartColorList.put("Transport", "#475bbf");
        categoryChartColorList.put("Other", "#8a3cb5");

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
                currencyLabel.setText(user.getCurrencySymbol());
                usernameLabel.setText(user.getUsername());

                if(user.getProfilePicture() == 0){
                    profileImage = new Image(getClass().getResourceAsStream("/assets/user-solid.png"));
                } else if(user.getProfilePicture() == 1) {
                    profileImage = new Image(getClass().getResourceAsStream("/assets/profileImage1.png"));
                } else if(user.getProfilePicture() == 2) {
                    profileImage = new Image(getClass().getResourceAsStream("/assets/profileImage2.png"));
                } else if(user.getProfilePicture() == 3) {
                    profileImage = new Image(getClass().getResourceAsStream("/assets/profileImage3.png"));
                } else if(user.getProfilePicture() == 4) {
                    profileImage = new Image(getClass().getResourceAsStream("/assets/profileImage4.png"));
                }
                goToProfileBtn.setImage(profileImage);

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
        addBtn.setDisable(true);
        clearBtn.setDisable(true);
        noBudgetLabel.setVisible(true);
        noBudgetBlur.setVisible(true);
//        categoriesVBox.setVisible(false);
        balanceLabel.setText("-");
        spentLabel.setText("-");

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

                String getCategoryInfo = "SELECT * FROM BudgetCategoryAmounts WHERE budgetId = '" + budget.getID() + "'";

                statement = connectDB.createStatement();
                queryResult = statement.executeQuery(getCategoryInfo);

                HashMap<Integer, Double> categoryAmountMap = new HashMap<>();

                while (queryResult.next()) {
                    categoryAmountMap.put((queryResult.getInt("categoryId")), queryResult.getDouble("limit"));
                }
                budget.setCategoryBudget(categoryAmountMap);

                balanceLabel.setText(budget.getTotalAmount()-budget.getTotalAmountSpent() + user.getCurrencySymbol());
                spentLabel.setText(budget.getTotalAmountSpent() + user.getCurrencySymbol());
                budget.setExpenseHistory();

                addBtn.setDisable(false);
                clearBtn.setDisable(false);
                noBudgetLabel.setVisible(false);
                noBudgetBlur.setVisible(false);
                setExpenseList();
                initializeNewExpenseBox();
                initializePieChart();
                System.out.println("budget set");
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

    private void initializePieChart() {
        pieChart.getData().clear();  // Clear existing data

        if(budget.getTotalAmountSpent() != 0) {

            pieChartBlur.setVisible(false);
            noExpenseLabel.setVisible(false);

            for (int i = 1; i <= 6; i++) {
                String categoryName = categoryNameList.get(i);
                Double categorySpentAmount = budget.getCategorySpent().get(i);

                if (categorySpentAmount != null && budget.getTotalAmountSpent() > 0) {
                    if (categorySpentAmount == 0) {
                        continue;
                    }
                    double percentage = categorySpentAmount / budget.getTotalAmountSpent() * 100;
                    String label = String.format("%s (%.2f%%)", categoryName, percentage);
                    pieChart.getData().add(new PieChart.Data(label, percentage));
                } else {
                    System.out.println("Category " + categoryName + " has no amount or totalAmount is 0");
                }
            }

            for (PieChart.Data data : pieChart.getData()) {
                data.getNode().setStyle("-fx-pie-color: " + categoryChartColorList.get(data.getName().split(" ")[0]) + ";");

            }
        }else{
            pieChartBlur.setVisible(true);
            noExpenseLabel.setVisible(true);
        }
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


    private void addPaymentRow(Date date, String name, String amount, String category) {
        Label dateLabel = new Label(date.toString());
        dateLabel.setFont(new Font("Segoe UI Semibold", 15));
        dateLabel.setStyle("-fx-text-fill: #3700b3;");

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font("Segoe UI Semibold", 15));
        nameLabel.setStyle("-fx-text-fill: #3700b3;");

        Label amountLabel = new Label(amount + user.getCurrencySymbol());
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

        Separator separator = new Separator();

        paymentListBox.getChildren().addFirst(row);
    }

    private void addPaymentRowAndSave(Date date, String name, String amount, String category) {
        addPaymentRow(date, name, amount, category);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        double parsedAmount = Double.parseDouble(amount);

        String insertFields = "INSERT INTO PaymentHistory(budgetId, userId, categoryId, name, amount, paymentDate) VALUES ('";
        String insertValues = budget.getID() + "','" + user.getID() + "','" + categoryIDList.get(category) + "','" + name + "','" + amount + "','" + date + "')";
        String insertToPaymentList = insertFields + insertValues;

        String updateBudgetSpent = "UPDATE Budgets SET totalAmountSpent = totalAmountSpent + " + parsedAmount + " WHERE budgetid = " + budget.getID();
        String updateCategorySpent = "UPDATE BudgetCategoryAmounts SET amount = amount + " + parsedAmount + " WHERE budgetid = " + budget.getID() + " AND categoryId = " + categoryIDList.get(category);

        try {
            Statement statement = connectDB.createStatement();

            statement.executeUpdate(insertToPaymentList);
            System.out.println("Expense added!");

            statement.executeUpdate(updateBudgetSpent);
            System.out.println("Budget amount spent updated");

            statement.executeUpdate(updateCategorySpent);
            System.out.println("Category amount spent updated");

            String amountSpentQuery = "SELECT totalAmountSpent FROM Budgets where budgetid = " + budget.getID();

            ResultSet rs = statement.executeQuery(amountSpentQuery);

            Double convertedAmount = 0.0;

            if(rs.next()){
                convertedAmount = rs.getDouble("totalAmountSpent");

                spentLabel.setText(convertedAmount + user.getCurrencySymbol());
                balanceLabel.setText(budget.getTotalAmount()-convertedAmount + user.getCurrencySymbol());
                budget.setExpenseHistory();
                budget.setTotalAmountSpent(convertedAmount);
                initializePieChart();

            }



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
            expenseInfoLabel.setText("Name: " + expenseNameTxt.getText() + ", Amount: " + amountTxt.getText() + user.getCurrencySymbol() +", Category: " + categoryBox.getValue());

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

    @Override
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        ProfilePageController controller = loader.getController();
        controller.initializeCurrencyComboBox();
        controller.setUser(user);
        controller.dragWindow(stage);
        controller.homepageNavAnimationIn();

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }


    @Override
    public void switchToBudgetCalc(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        WallieAiController controller = loader.getController();
        controller.setUser(user);
        controller.dragWindow(stage);
        controller.homepageNavAnimationIn(event);

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    public void profileNavAnimationOut(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
        Parent newRoot = loader.load();

        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 1.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(266);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
            try {
                switchToProfile(event, newRoot, loader);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        anim.play();
    }

    public void profileNavAnimationIn(){
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusGradient.setTranslateX(focusGradient.getTranslateX() + 266);

        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(1);
        whiteOutAnim.setToValue(0);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(-266);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void wallieAiNavAnimationIn(MouseEvent event) throws IOException{
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusGradient.setTranslateX(focusGradient.getTranslateX() - 266);

        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(1);
        whiteOutAnim.setToValue(0);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(266);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void wallieAiNavAnimationOut(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WallieAiPage.fxml"));
        Parent newRoot = loader.load();

        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);

        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH))
        );

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(-266);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
            try {
                switchToBudgetCalc(event, newRoot, loader);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        anim.play();
    }

    private void updateGradient(double focusDistance) {
        focusGradient.setFill(new RadialGradient(
                0,
                focusDistance,
                0.5, 0.5,
                0.48,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#b787ff")),
                new Stop(1, Color.TRANSPARENT)
        ));
    }

    @Override
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException{

    }

    @Override
    public void closeApp() {
        stage = (Stage) expenseNameTxt.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeApp() {
        stage = (Stage) expenseNameTxt.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void dragWindow(Stage stage) {
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        topBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
