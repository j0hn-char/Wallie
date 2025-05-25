package application.walliedev;

public class User {
    private String username;
    private String password;
    private String email;
    private String currency;
    private int budget; //change type to budget when budget class created
    private int profilePicture; //ID(number) of the picture selected by the user

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currency = "€";
        this.profilePicture = 1;
    }

    public User(String username, String password, String email, String currency, int profilePicture, int budget) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currency = currency;
        this.profilePicture = profilePicture;
        this.budget = budget;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    boolean checkPasswords(String password1, String password2){
        return password1.equals(password2);
    }
}