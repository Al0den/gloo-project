package core;

import users.User;
import users.Customer;
import users.Manager;
import users.Cashier;
import inventory.Category;
import inventory.Inventory;
import inventory.Item;
import users.Cart;
import payment.POSDevice;
import payment.TransactionSystem;
import payment.BankCard;
import discount.DiscountPolicy;
import discount.DiscountPolicyFactory;
import delivery.DeliveryFeePolicy;
import delivery.WeightDistanceDeliveryFee;
import delivery.DeliveryRequest;
import delivery.DeliveryScheduler;
import delivery.DeliverySlot;
import delivery.DistanceCalculator;
import delivery.LevenschteinDistanceCalculator;
import payment.Bill;

import java.util.Map;
import java.util.HashMap;

public class Supermarket {
    private static final String SUPERMARKET_ADDRESS = "14 Mail Pierre Potier, Gif sur Yvette";

    private DistanceCalculator distanceCalculator;
    private Map<String, User> users; //username -> User
    private Inventory inventory;

    private double revenue = 0.0;
    private TransactionSystem tas;
    private POSDevice pos;

    private DeliveryFeePolicy deliveryFeePolicy;
    private DeliveryScheduler deliveryScheduler;
    
    public Supermarket() {
        users = new HashMap<>();
        inventory = new Inventory(new HashMap<>());
        deliveryScheduler = new DeliveryScheduler();
        distanceCalculator = new LevenschteinDistanceCalculator();

        tas = new TransactionSystem();
        pos = new POSDevice(tas);

        registerManager("ceo", "ceo", "ceo", "123456789");

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
        getCategoryOrCreate("dairy");
        getCategoryOrCreate("fruit-and-vegetables");
        getCategoryOrCreate("meat");

        if (!userExists("cashier")) {
            registerCashier("Default", "Cashier", "cashier", "cashier");
        }

        if (!userExists("customer")) {
            registerCustomer("Default", "Customer", "customer", "1 Main St", "customer", "normal");
        }

        tas.registerCard(new BankCard("4242424242424242", "12345", 1000.0));
        tas.registerCard(new BankCard("1111222233334444", "0000", 5.0));
    }

    public TransactionSystem getTransactionSystem() {
        return tas;
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
        this.getCategoryOrCreate(categoryName);
        
        if (inventory.getItem(categoryName, itemName) != null) {
            throw new IllegalArgumentException("Item already exists in category");
        }

        Item item = new Item(itemName, price, weight, stock);
        inventory.addItem(categoryName, item);
    }

    public void restock(String itemName, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Item item = getItem(itemName);
        if (item == null) {
            throw new IllegalArgumentException("Item does not exist");
        }

        item.setStock(item.getStock() + quantity);
    }

    public Bill computeBill(Customer customer, Cart cart) {
        Bill bill = new Bill(customer);
        DiscountPolicy discountPolicy = customer.getDiscountPolicy();

        double itemsTotal = cart.computeTotalAndFillBill(discountPolicy, bill);

        double deliveryFee = 0.0;
        if (customer.hasRequestedDelivery()) {
            DeliveryRequest deliveryRequest = customer.getDeliveryRequest();
            double baseDeliveryFee = deliveryFeePolicy.computeFee(cart, itemsTotal, deliveryRequest);
            deliveryFee = customer.getDiscountPolicy().applyDeliveryDiscount(baseDeliveryFee);
        }

        bill.setDeliveryFee(deliveryFee);

        return bill;
    }

    public void subscribeToPlan(Customer customer, String planName) {
        DiscountPolicy newPolicy = DiscountPolicyFactory.create(planName);


        double oneTimeFee = newPolicy.getOneTimeFee();
        addRevenue(oneTimeFee); // We currently dont have a way to pay this fee, but we can at least add it to the revenue of the supermarket
        
        customer.setDiscountPolicy(newPolicy);
    }

    public DeliveryScheduler getDeliveryScheduler() {
        return deliveryScheduler;
    }

    public void requestDelivery(Customer customer, String address, String time) {
        if (!time.equals("morning") && !time.equals("lunch") && !time.equals("afternoon") && !time.equals("evening")) {
            throw new IllegalArgumentException("Invalid delivery time: " + time + ". Valid options are: morning, lunch, afternoon, evening.");
        }

        double distance = distanceCalculator.calculateDistance(address, SUPERMARKET_ADDRESS);

        DeliverySlot slot;
        try {
            slot = deliveryScheduler.getSlot(time);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid delivery time: " + time + ". Valid options are: morning, lunch, afternoon, evening.");
        }

        customer.requestDelivery(address, distance, slot);
    }

    public Bill finalizeSale(Customer customer, Cart cart, String cardNumber, String pin) {
        Bill bill = computeBill(customer, cart);

        payment.PaymentResult result = getPosDevice().processPayment(cardNumber, pin, bill.getFinalAmount());

        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return null;
        }

        if (customer.hasRequestedDelivery()) {
            DeliveryRequest request = customer.getDeliveryRequest();
            double weight = cart.getTotalWeight();
            deliveryScheduler.bookSlot(request.getSlot().getSlotId(), weight);
        }

        bill.setStatus(true);

        addRevenue(bill.getFinalAmount());

        cart.finalizeSale();
        customer.clearDeliveryRequest();



        return bill;
    }
}
