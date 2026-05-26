package users;

public class User {
    private static int nextId = 1;

    private int id;
    private String firstName;
    private String surname;
    private String username;
    private String password;

    public User(String username, String firstName, String surname, String password) {
        this.id = nextId++;
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getId() {
        return id;
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
        return "None";
    }
}
