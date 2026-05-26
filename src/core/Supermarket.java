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

        discountPlans.put("Default", new DiscountPlan("Default", 0.0));

        addManager("ceo", "ceo", "ceo", "123456789");
    }

    public void addDiscountPlan(String planName, double globalDiscountPercentage) {
        discountPlans.put(planName, new DiscountPlan(planName, globalDiscountPercentage));
    }

    public void addCustomer(String username, String firstName, String surname, String address, String password, String planName) {
        Customer customer = new Customer(username, firstName, surname, password, address, discountPlans.get(planName));
        users.put(customer.getUsername(), customer);
    }

    public void addCustomer(String username, String firstName, String surname, String address, String password) {
        addCustomer(username, firstName, surname, address, password, "Default");
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
}
