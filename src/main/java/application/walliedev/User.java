package application.walliedev;

public class User {
    private String username;
    private String password;
    private String email;
    private String currency;
    private Budget budget;
    private int profilePicture; //ID(number) of the picture selected by the user

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currency = "€";
        this.profilePicture = 1;
    }

    boolean checkPasswords(String password1, String password2){
        return password1.equals(password2);
    }
}
