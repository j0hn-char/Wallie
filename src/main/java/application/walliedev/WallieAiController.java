package application.walliedev;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.transform.Result;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class WallieAiController implements NavBar, AppControls, Form{

    @FXML
    private HBox topBar;

    @FXML
    private MFXButton wallieAiBtn;

    @FXML
    private ImageView homePageLogo, goToProfileBtn;

    @FXML
    private Rectangle focusGradient, whiteOut;

    @FXML
    private Label usernameLabel, errorLabel;

    @FXML
    private MFXButton calculateBtn;

    @FXML
    private MFXTextField budgetAmountTxt, fixedExpensesTxt;

    private double budgetAmount;
    private double fixedExpenses;
    private double totalBudget;

    private User user;
    private Budget budget;
    private Stage stage;
    private Parent root;
    private double xOffset = 0;
    private double yOffset = 0;
    private final DoubleProperty focusDistance = new SimpleDoubleProperty(0);

    public void setUser(User user) {
        this.user=user;
        usernameLabel.setText(user.getUsername());
    }

    public void setBudget(Budget budget) {
        this.budget=budget;
    }

    @Override
    public void switchToHomepage(MouseEvent event, String username, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        HomepageController controller = loader.getController();
        controller.initializeCategoryLists();
        controller.setUser(username);
        controller.dragWindow(stage);
        controller.wallieAiNavAnimationIn(event);

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @Override
    public void switchToProfile(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        ProfilePageController controller = loader.getController();
        controller.initializeCurrencyComboBox();
        controller.setUser(user);
        controller.dragWindow(stage);
        controller.wallieAiNavAnimationIn();

        root.getStylesheets().add(getClass().getResource("/custom-materialfx.css").toExternalForm());

        Scene currentScene = ((Node)event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @Override
    public void switchToBudgetCalc(MouseEvent event, Parent root, FXMLLoader loader) throws IOException {

    }

    public void homepageNavAnimationIn(MouseEvent event) throws IOException {
        whiteOut.setVisible(true);
        goToProfileBtn.setDisable(true);
        wallieAiBtn.setDisable(true);
        homePageLogo.setDisable(true);
        focusDistance.addListener((obs, oldVal, newVal) -> updateGradient(newVal.doubleValue()));

        Timeline focusAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(focusDistance, -1.0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(focusDistance, 0.0, Interpolator.EASE_BOTH))
        );

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(0.7), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(-266);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(0.7), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(1);
        whiteOutAnim.setToValue(0);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
    }

    public void homepageNavAnimationOut(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
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
                switchToHomepage(event, user.getUsername(), newRoot, loader);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        anim.play();
    }

    public void profileNavAnimationIn(MouseEvent event) throws IOException {
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

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(1.4), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_OUT);
        moveGradient.setByX(-532);

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(1.4), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_OUT);
        whiteOutAnim.setFromValue(1);
        whiteOutAnim.setToValue(0);

        ParallelTransition anim = new ParallelTransition(focusAnim, moveGradient, whiteOutAnim);
        anim.setOnFinished(e -> {
            whiteOut.setVisible(false);
            goToProfileBtn.setDisable(false);
            wallieAiBtn.setDisable(false);
            homePageLogo.setDisable(false);
        });
        anim.play();
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

        FadeTransition whiteOutAnim = new FadeTransition(Duration.seconds(1.4), whiteOut);
        whiteOutAnim.setInterpolator(Interpolator.EASE_IN);
        whiteOutAnim.setFromValue(0);
        whiteOutAnim.setToValue(1);

        TranslateTransition moveGradient = new TranslateTransition(Duration.seconds(1.4), focusGradient);
        moveGradient.setInterpolator(Interpolator.EASE_IN);
        moveGradient.setByX(532);

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
    public void closeApp() {
        stage = (Stage) topBar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeApp() {
        stage = (Stage) topBar.getScene().getWindow();
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

    public void calculateBudget() throws Exception {

        if(checkFields()) {
            budgetAmount = Double.parseDouble(budgetAmountTxt.getText());

            if(fixedExpensesTxt.getText().trim().isEmpty()) {
                fixedExpenses = 0;
            } else {
                fixedExpenses = Double.parseDouble(fixedExpensesTxt.getText());
            }
            totalBudget = budgetAmount - fixedExpenses;

            try {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();

                String budgetQuery = "I have a budget of " + totalBudget + ". I want you to make me a distribution of my money in the following categories based on their importance and anything else you consider important. The categories are as follows. Health, Home, Leisure, Shopping, Tranport, Other.";


                Statement statement = connectDB.createStatement();


                if(checkBudgetExistance())
                {
                    String prevBudgetQuery = "The specific user in the previous budget had spent 30%, 10%, 15%, 25%, 5%, 15% of his expenses in each category respectively.";
                    budgetQuery = budgetQuery + prevBudgetQuery;

                    String deleteBudget = "DELETE FROM Budgets where userID ='" + user.getID() + "'";

                    try {
                        statement = connectDB.createStatement();
                        statement.executeUpdate(deleteBudget);
                        System.out.println("Deleted budget");

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
                String formattingQuery = "I want you to return it to me in json. Give it to me without writing anything extra and without using text formatting or markdown.";

                String aiResponse = OpenAi.getResponse(budgetQuery + formattingQuery);

                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> json = gson.fromJson(aiResponse, type);

                statement = connectDB.createStatement();

                String insertBudget = "INSERT INTO Budgets(userId, totalAmount, totalAmountSpent) VALUES('" + user.getID() + "','" + totalBudget + "','" + 0 + "')";

                statement.executeUpdate(insertBudget);
                System.out.println("New Budget created!!");

                String getBudgetInfo = "SELECT * FROM budgets WHERE userId = '" + user.getID() + "'";
                ResultSet queryResult = statement.executeQuery(getBudgetInfo);
                queryResult = statement.executeQuery(getBudgetInfo);
                queryResult.next();
                budget = new Budget(
                        queryResult.getInt("budgetId"),
                        queryResult.getDouble("totalAmount"),
                        queryResult.getDouble("totalAmountSpent")
                );

                for(Map.Entry<String, Object> entry : json.entrySet()){
                    String categoryName = entry.getKey();
                    double limit = (double) entry.getValue();

                    statement = connectDB.createStatement();

                    //String insertCategoryLimit = "INSERT INTO BudgetCategoryAmounts(budgetId, categoryId, amount, limit) VALUES('" + budget.getID() + "'," + "(SELECT categoryID FROM budgetcategories WHERE name = '" + categoryName +"'),'" + 0.0 +"','" + limit +"'");
                    String insertCategoryLimit = "INSERT INTO BudgetCategoryAmounts (budgetId, categoryId, amount, `limit`) " +
                            "VALUES (" + budget.getID() + ", " +
                            "(SELECT categoryId FROM BudgetCategories WHERE name = '" + categoryName + "'), " +
                            "0.0, " + limit + ")";


                    statement.executeUpdate(insertCategoryLimit);

                    System.out.println(categoryName + " " + limit);
                }



            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkBudgetExistance(){

        boolean flag = false;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String getBudgetInfo = "SELECT * FROM budgets WHERE userId = '" + user.getID() + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getBudgetInfo);

            if(queryResult.next()){
                flag = true;
            }
            else {
                flag = false;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return flag;
    }

    @Override
    public boolean checkFields() {

        boolean flag = true;
        budgetAmountTxt.getStyleClass().remove("error-field");
        fixedExpensesTxt.getStyleClass().remove("error-field");
        errorLabel.setVisible(false);

        if(budgetAmountTxt.getText().trim().isEmpty() || !budgetAmountTxt.getText().trim().matches("\\d*\\.?\\d+")) {
            budgetAmountTxt.getStyleClass().add("error-field");
            errorLabel.setVisible(true);
            flag = false;
        }

        if(!fixedExpensesTxt.getText().trim().matches("\\d*\\.?\\d+")) {
            fixedExpensesTxt.getStyleClass().add("error-field");
            errorLabel.setVisible(true);
            flag = false;
        }

        if(flag && (Double.parseDouble(fixedExpensesTxt.getText()) > Double.parseDouble(budgetAmountTxt.getText()))){
            fixedExpensesTxt.getStyleClass().add("error-field");
            budgetAmountTxt.getStyleClass().add("error-field");
            errorLabel.setText("Fixed expenses can't be greater than budget amount");
            errorLabel.setVisible(true);
            flag = false;
        }

        return flag;
    }

}