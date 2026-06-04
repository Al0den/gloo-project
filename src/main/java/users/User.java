package users;

public class User {
    protected String firstName;
    protected String surname;
    protected String username;
    protected String password;

    public User(String username, String firstName, String surname, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return "User";
    }
}
