package application.walliedev;

import java.util.Date;

public class Expense {
    private String name;
    private double amount;
    private Date date;
    private int categoryId;

    public Expense(String name, double amount, Date date, int category) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.categoryId = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
