package application.walliedev;

public class Expense {
    private double amount;
    private String date;
    private Category category;

    public Expense(double amount, String date, Category category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}
