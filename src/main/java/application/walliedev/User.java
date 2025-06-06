package application.walliedev;

public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private int currency;
    private int profilePicture; //ID(number) of the picture selected by the user

    public User(int userID, String username, String password, String email, int currency, int profilePicture) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.currency = currency;
        this.profilePicture = profilePicture;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    private boolean checkPasswords(String password1, String password2){
        return password1.equals(password2);
    }

    public int getCurrency() {
        return currency;
    }

    public int getID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() { return email;
    }
}