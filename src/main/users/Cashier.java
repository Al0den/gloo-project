package users;

public class Cashier extends User {
    public Cashier(String username, String firstName, String surname, String password) {
        super(username, firstName, surname, password);
    }

    @Override
    public String getRole() {
        return "Cashier";
    }
    
}
