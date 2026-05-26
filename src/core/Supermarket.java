package core;

import users.User;
import users.Customer;
import users.Manager;
import users.Cashier;
import catalog.Catalog;
import catalog.Category;
import catalog.Item;
import discount.DiscountPlan;

import java.util.Map;
import java.util.HashMap;

public class Supermarket {
    private Map<String, User> users; //username -> User
    private Map<String, DiscountPlan> discountPlans;
    private Catalog catalog;
    
    public Supermarket() {
        users = new HashMap<>();
        discountPlans = new HashMap<>();
        catalog = new Catalog(new HashMap<>());

        addManager("ceo", "ceo", "ceo", "123456789");
    }

    public void addDiscountPlan(String planName, double globalDiscountPercentage, double globalDiscountMinimumCeiling, double oneTimeFee) {
        discountPlans.put(planName, new DiscountPlan(planName, globalDiscountPercentage, globalDiscountMinimumCeiling, oneTimeFee));
    }

    public void addCustomer(String username, String firstName, String surname, String address, String password, String planName) {
        Customer customer = new Customer(username, firstName, surname, address, password, discountPlans.get(planName));
        users.put(customer.getUsername(), customer);
    }

    public void addCustomer(String username, String firstName, String surname, String address, String password) {
        addCustomer(username, firstName, surname, address, password, "normal");
    }

    public void addManager(String username, String firstName, String surname, String password) {
        Manager manager = new Manager(username, firstName, surname, password);
        users.put(manager.getUsername(), manager);
    }

    public void addCashier(String username, String firstName, String surname, String password) {
        Cashier cashier = new Cashier(username, firstName, surname, password);
        users.put(cashier.getUsername(), cashier);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Category getCategoryOrCreate(String categoryName) {
        Category category = catalog.getCategory(categoryName);
        if (category == null) {
            catalog.addCategory(categoryName);
            category = catalog.getCategory(categoryName);
        }
        return category;
    }

    public void addItem(String categoryName, Item item) {
        Category cat = getCategoryOrCreate(categoryName);
        cat.addItem(item);
    }

    public void setup() {
        getCategoryOrCreate("dairy");
        getCategoryOrCreate("fruit-and-vegetables");
        getCategoryOrCreate("meat");

        addDiscountPlan("prime", 20, 50, 50);
        addDiscountPlan("platinum", 30, 0, 200);
        addDiscountPlan("normal", 0, 0, 0);
   
    }

    public DiscountPlan getDiscountPlan(String planName) {
        return discountPlans.get(planName);
    }
}
