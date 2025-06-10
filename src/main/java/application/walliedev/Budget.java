package application.walliedev;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Budget {
    private int budgetID;
    private double totalAmount;
    private double totalAmountSpent;
    private ArrayList<Expense> expenseHistory = new ArrayList<>();

    public Budget(int budgetID, double totalAmount, double totalAmountSpent) {
        this.budgetID = budgetID;
        this.totalAmountSpent = totalAmountSpent;
        this.totalAmount = totalAmount;
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
                        rs.getInt("categoryId")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addExpense(Expense expense){
        this.expenseHistory.add(expense);
    }

    private void checkBalance(){

    }

    public int getID(){
        return budgetID;
    }

    private void checkCategoryBalance(){

    }

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
