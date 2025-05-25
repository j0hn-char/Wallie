package application.walliedev;

import java.util.ArrayList;

public class Budget {
    private double totalAmount;
    private double totalAmountSpent;
    private double categoryAmount;
    private double categorySpent;
    private ArrayList<Expense> expenseHistory;

    public Budget(double totalAmountSpent, double categoryAmount, double categorySpent, double totalAmount, ArrayList<Expense> expenseHistory) {
        this.totalAmountSpent = totalAmountSpent;
        this.categoryAmount = categoryAmount;
        this.categorySpent = categorySpent;
        this.totalAmount = totalAmount;
        this.expenseHistory = new ArrayList<>();
    }

    public void addExpense(Expense expense){
        this.expenseHistory.add(expense);
    }

    private void checkBalance(){

    }

    private void checkCategoryBalance(){

    }

    public ArrayList<Expense> getExpenseHistory(){

    }
}
