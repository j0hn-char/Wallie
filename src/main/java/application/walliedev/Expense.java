package application.walliedev;

public class Expense {
    private double amount;
    private String date;
    private String category; //change type to category when category class is created

    public Expense(double amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}
