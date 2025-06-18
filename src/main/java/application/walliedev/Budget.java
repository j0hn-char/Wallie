package application.walliedev;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Budget {
    private int budgetID;
    private double totalAmount;
    private double totalAmountSpent;
    private ArrayList<Expense> expenseHistory = new ArrayList<>();
    private HashMap<Integer, Double> categoryBudget = new HashMap<>();
    private HashMap<Integer, Double> categorySpent = new HashMap<>();

    public Budget(int budgetID, double totalAmount, double totalAmountSpent) {
        this.budgetID = budgetID;
        this.totalAmountSpent = totalAmountSpent;
        this.totalAmount = totalAmount;

        categorySpent.put(1, 0.0);
        categorySpent.put(2, 0.0);
        categorySpent.put(3, 0.0);
        categorySpent.put(4, 0.0);
        categorySpent.put(5, 0.0);
        categorySpent.put(6, 0.0);
    }

    public void setExpenseHistory(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String getExpenseHistoryInfo = "SELECT * FROM PaymentHistory WHERE budgetId = '" + this.budgetID + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(getExpenseHistoryInfo);

            while(rs.next()) {
                expenseHistory.add(new Expense(
                        rs.getString("name"),
                        rs.getDouble("amount"),
                        rs.getDate("paymentDate"),
                        rs.getInt("categoryId") //test
                ));
            }

            String getCategorieInfo = "SELECT * FROM BudgetCategoryAmounts WHERE budgetId = '" + this.budgetID + "'";
            rs = statement.executeQuery(getCategorieInfo);

            while(rs.next()) {
                categoryBudget.put(rs.getInt("categoryId"), rs.getDouble("limit"));
                categorySpent.put(rs.getInt("categoryId"), rs.getDouble("amount"));
            }


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addExpense(Expense expense){
        this.expenseHistory.add(expense);
    }

    private void checkBalance(){}

    public int getID(){
        return budgetID;
    }

    public void setCategoryBudget(HashMap<Integer, Double> categoryBudget){
        this.categoryBudget = categoryBudget;
    }

    public HashMap<Integer, Double> getCategoryBudget(){
        return categoryBudget;
    }

    private void checkCategoryBalance(){}

    public ArrayList<Expense> getExpenseHistory(){
        return expenseHistory;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }
}
