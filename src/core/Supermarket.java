package core;

import users.User;
import users.Customer;
import users.Manager;
import users.Cashier;
import catalog.Catalog;
import catalog.Category;
import catalog.Item;
import discount.DiscountPolicyFactory;
import payment.POSDevice;
import payment.TransactionSystem;
import payment.BankCard;

import java.util.Map;
import java.util.HashMap;

public class Supermarket {
    private Map<String, User> users; //username -> User
    private Catalog catalog;

    private double revenue = 0.0;
    private TransactionSystem tas;
    private POSDevice pos;
    
    public Supermarket() {
        users = new HashMap<>();
        catalog = new Catalog(new HashMap<>());

        tas = new TransactionSystem();
        pos = new POSDevice(tas);

        addManager("ceo", "ceo", "ceo", "123456789");
    }

    public void addCustomer(String username, String firstName, String surname, String address, String password, String planName) {
        Customer customer = new Customer(username, firstName, surname, address, password, DiscountPolicyFactory.create(planName));
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

    public Item getItem(String itemName) {
        return catalog.getItem(itemName);
    }

    public Category getItemCategory(String itemName) {
        return catalog.getItemCategory(itemName);
    }

    public void setup() {
        getCategoryOrCreate("dairy");
        getCategoryOrCreate("fruit-and-vegetables");
        getCategoryOrCreate("meat");

        tas.registerCard(new BankCard("4242424242424242", "12345", 1000.0));
        tas.registerCard(new BankCard("1111222233334444", "0000", 5.0));
    }

    public POSDevice getPosDevice() {
        return pos;
    }

    public void addRevenue(double amount) {
        revenue += amount;
    }

    public double getRevenue() {
        return revenue;
    }

    public Map<String, Item> getInventory() {
        Map<String, Item> inventory = new HashMap<>();

        for (Category category : catalog.getCategories().values()) {
            inventory.putAll(category.getItems());
        }

        return inventory;
    }
}
