package application.walliedev;

public class Expense {
    private double amount;
    private String date;
    private String category;

    public Expense(double amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}
