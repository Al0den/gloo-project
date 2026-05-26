package users;

public class Manager extends User {

    public Manager(String username, String firstName, String surname, String password) {
        super(username, firstName, surname, password);
    }

    @Override
    public String getRole() {
        return "Manager";
    }

}
