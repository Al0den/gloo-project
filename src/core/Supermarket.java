package core;

import users.User;
import users.Customer;
import users.Manager;
import users.Cashier;
import discount.DiscountPolicyFactory;
import inventory.Category;
import inventory.Inventory;
import inventory.Item;
import users.Cart;
import payment.POSDevice;
import payment.TransactionSystem;
import payment.BankCard;
import discount.DiscountPolicy;
import delivery.DeliveryFeePolicy;
import delivery.WeightDistanceDeliveryFee;
import delivery.DeliveryRequest;

import java.util.Map;
import java.util.HashMap;

public class Supermarket {
    private Map<String, User> users; //username -> User
    private Inventory inventory;

    private double revenue = 0.0;
    private TransactionSystem tas;
    private POSDevice pos;

    private DeliveryFeePolicy deliveryFeePolicy;
    
    public Supermarket() {
        users = new HashMap<>();
        inventory = new Inventory(new HashMap<>());

        tas = new TransactionSystem();
        pos = new POSDevice(tas);

        registerManager("ceo", "ceo", "ceo", "123456789");

        // Here, we wil only use the delivery fee Weight Distance based
        deliveryFeePolicy = new WeightDistanceDeliveryFee();
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Category getCategoryOrCreate(String categoryName) {
        Category category = inventory.getCategory(categoryName);
        if (category == null) {
            inventory.addCategory(categoryName);
            category = inventory.getCategory(categoryName);
        }
        return category;
    }

    public Item getItem(String itemName) {
        return inventory.getItem(itemName);
    }

    public Category getItemCategory(String itemName) {
        return inventory.getItemCategory(itemName);
    }

    public void setup() {
        getCategoryOrCreate("diary");
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

        for (Category category : this.inventory.getCategories().values()) {
            inventory.putAll(category.getItems());
        }

        return inventory;
    }

    public void registerManager(String firstName, String surname, String username, String password) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Manager manager = new Manager(username, firstName, surname, password);
        users.put(manager.getUsername(), manager);
    }

    public void registerCashier(String firstName, String surname, String username, String password) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Cashier cashier = new Cashier(username, firstName, surname, password);
        users.put(cashier.getUsername(), cashier);
    }

    public void registerCustomer(String firstName, String surname, String username, String address, String password, String planName) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Customer customer = new Customer(username, firstName, surname, address, password, DiscountPolicyFactory.create(planName));
        users.put(customer.getUsername(), customer);
    }

    public void setCategoryDiscount(String categoryName, double discountPercentage) {
        Category category = inventory.getCategory(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category does not exist");
        }

        category.setPricingPolicy(new inventory.PercentageCategoryPricingPolicy(discountPercentage));
    }

    public void addItem(String categoryName, String itemName, double price, double weight, int stock) {
        if (inventory.getCategory(categoryName) == null) {
            throw new IllegalArgumentException("Category does not exist");
        }

        if (inventory.getItem(categoryName, itemName) != null) {
            throw new IllegalArgumentException("Item already exists in category");
        }

        Item item = new Item(itemName, price, weight, stock);
        inventory.addItem(categoryName, item);
    }

    public Double computeBill(Customer customer, Cart cart) {
        DiscountPolicy discountPolicy = customer.getDiscountPolicy();

        double itemsTotal = cart.getTotalPrice(discountPolicy);

        double deliveryFee = 0.0;
        if (customer.hasRequestedDelivery()) {
            DeliveryRequest deliveryRequest = customer.getDeliveryRequest();
            double baseDeliveryFee = deliveryFeePolicy.computeFee(cart, itemsTotal, deliveryRequest);
            deliveryFee = customer.getDiscountPolicy().applyDeliveryDiscount(baseDeliveryFee);
        }

        return itemsTotal + deliveryFee;
    }
}
