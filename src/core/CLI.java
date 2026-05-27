package core;

import catalog.Category;
import catalog.Item;
import users.User;
import users.Customer;
import discount.DiscountPlan;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CLI {
    private Supermarket supermarket;
    private Session session;

    public CLI(Supermarket supermarket) {
        this.supermarket = supermarket;
        this.session = new Session();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Supermarket CLI");
        System.out.println("Type help to see available commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("stop") || input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }

            executeCommand(input);
        }

        scanner.close();
    }

    private void executeCommand(String input) {
        String[] parts = parseArguments(input);
        if (parts.length == 0) {
            return;
        }

        String command = parts[0].toLowerCase();

        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        switch (command) {
            case "help":
                printHelp();
                break;
            case "login":
                login(args);
                break;
            case "logout":
                session.logout();
                System.out.println("Logged out.");
                break;
            case "additem":
                addItem(args);
                break;
            case "setcategorydiscount":
                setCategoryDiscount(args);
                break;
            case "registercustomer":
                registerCustomer(args);
                break;
            case "registermanager":
                registerManager(args);
                break;
            case "registercashier":
                registerCashier(args);
                break;
            case "runfile":
                runFile(args);
                break;
            case "setup":
                supermarket.setup();
                System.out.println("Supermarket setup completed.");
                break;
            case "subscribetoplan":
                subscribeToPlan(args);
                break;
            case "requestdelivery":
                requestDelivery(args);
                break;
            case "startcheckout":
                startCheckout(args);
                break;
            case "scanitem":
                scanItem(args);
                break;
            case "computebill":
                computeBill(args);
                break;
            case "simulatepayment":
                simulatePayment(args);
                break;
            case "pay":
                pay(args);
                break;
            case "showrevenue":
                showRevenue(args);
                break;
            case "showinventory":
                showInventory(args);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void pay(String[] args) {
        if (!session.isCashier()) {
            System.out.println("Only cashiers can launch payment.");
            return;
        }

        if (!session.hasActiveCheckout()) {
            System.out.println("No active checkout.");
            return;
        }

        if (!session.hasComputedBill()) {
            System.out.println("Compute the bill before payment.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Usage: pay <cardNumber> <pin>");
            return;
        }

        String cardNumber = args[0];
        String pin = args[1];
        double amount = session.getCurrentBill();

        payment.PaymentResult result = supermarket.getPosDevice().processPayment(cardNumber, pin, amount);

        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return;
        }

        supermarket.addRevenue(amount);

        session.getCurrentCart().finalizeSale();

        System.out.println("Payment accepted.");
        System.out.println("Receipt:");
        System.out.println("Customer: " + session.getCheckoutCustomer().getUsername());
        System.out.println("Total paid: " + amount);

        session.endCheckout();
    }

    private void showRevenue(String[] args) {
        if (!session.isManager()) {
            System.out.println("Only managers can view revenue.");
            return;
        }

        System.out.println("Total revenue: " + supermarket.getRevenue());
    }

    private void showInventory(String[] args) {
        if (!session.isManager()) {
            System.out.println("Only managers can view inventory.");
            return;
        }

        System.out.println("Current inventory:");
        for (Map.Entry<String, Item> entry : supermarket.getInventory().entrySet()) {
            String itemName = entry.getKey();
            Item item = entry.getValue();
            System.out.println(itemName + " - Price: " + item.getPrice() + ", Stock: " + item.getStock());
        }
    }

    private void simulatePayment(String[] args) {
        if (!session.isCashier()) {
            System.out.println("Only cashiers can simulate payments.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Usage: simulatePayment <SUCCESS|INSUFFICIENT_FUNDS|PIN_WRONG|AUTH_DENIED>");
            return;
        }

        try {
            payment.PaymentOutcome outcome = payment.PaymentOutcome.valueOf(args[0].toUpperCase());
            supermarket.getPosDevice().simulateNextPayment(outcome);
            System.out.println("Next payment forced to: " + outcome);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid payment outcome: " + args[0]);
        }
    }

    private void computeBill(String[] args) {
        if (!session.isCashier()) {
            System.out.println("Only cashiers can compute bills.");
            return;
        }

        if (!session.hasActiveCheckout()) {
            System.out.println("No active checkout. Use startCheckout to begin.");
            return;
        }
        DiscountPlan plan = session.getCheckoutCustomer().getDiscountPlan();
        double total = session.getCurrentCart().getTotalPrice(plan);

        session.setCurrentBill(total);

        System.out.println("Total bill: " + total);
    }

    private void scanItem(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: scanItem <itemName> <quantity>");
            return;
        }

        if (!session.isCashier()) {
            System.out.println("Only cashiers can scan items.");
            return;
        }

        if (!session.hasActiveCheckout()) {
            System.out.println("No active checkout. Use startCheckout to begin.");
            return;
        }

        String itemName = args[0];
        Item item = supermarket.getItem(itemName);
        if (item == null) {
            System.out.println("Item not found: " + itemName);
            return;
        }

        Category cat = supermarket.getItemCategory(itemName);

        int quantity;
        try {
            quantity = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a valid number.");
            return;
        }

        session.getCurrentCart().addItem(item, cat, quantity);
        System.out.println("Scanned item: " + item.getName() + " - Price: " + item.getPrice() * quantity);
    }

    private void startCheckout(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: startCheckout <customerUsername>");
            return;
        }

        if (!session.isCashier()) {
            System.out.println("Only cashiers can start checkout.");
            return;
        }

        String customerUsername = args[0];
        User user = supermarket.getUser(customerUsername);
        if (user == null || !(user instanceof Customer)) {
            System.out.println("Customer not found: " + customerUsername);
            return;
        }

        Customer customer = (Customer) user;
        session.startCheckout(customer);
        System.out.println("Started checkout for customer: " + customer.getFirstName() + " (" + customer.getUsername() + ")");
    }

    private void requestDelivery(String[] args) {
        if (session.getCurrentUser() == null || !session.getCurrentUser().getRole().equals("Customer")) {
            System.out.println("Only customers can request deliveries.");
            return;
        }

        Customer customer = (Customer) session.getCurrentUser();
        customer.hasRequestedDelivery();

        String address = customer.getAddress();
        System.out.println("Delivery requested to address: " + address);
    }

    private void subscribeToPlan(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: subscribeToPlan <planName>");
            return;
        }

        if (session.getCurrentUser() == null || !session.getCurrentUser().getRole().equals("Customer")) {
            System.out.println("Only customers can subscribe to discount plans.");
            return;
        }

        String planName = args[0];
        DiscountPlan plan = supermarket.getDiscountPlan(planName);
        if (plan == null) {
            System.out.println("Discount plan not found: " + planName);
            return;
        }

        Customer customer = (Customer) session.getCurrentUser();
        customer.setDiscountPlan(plan);
        System.out.println("Subscribed to discount plan: " + planName);
    }

    private void runFile(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: runFile <filename>");
            return;
        }

        String filename = args[0];
        try (Scanner fileScanner = new Scanner(new java.io.File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    System.out.println("> " + line);
                    executeCommand(line);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    private void registerManager(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: registerManager <firstName> <surname> <username> <password>");
            return;
        }

        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String address = args[3];
        String password = args[4];

        if (supermarket.userExists(username)) {
            System.out.println("Username already exists: " + username);
            return;
        }

        supermarket.addCustomer(username, firstName, surname, address, password);
        System.out.println("Registered manager: " + firstName + " " + surname + " (" + username + ")");
    }

    private void registerCashier(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: registerCashier <firstName> <surname> <username> <password>");
            return;
        }

        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String password = args[3];

        if (supermarket.userExists(username)) {
            System.out.println("Username already exists: " + username);
            return;
        }

        supermarket.addCashier(username, firstName, surname, password);
        System.out.println("Registered cashier: " + firstName + " " + surname + " (" + username + ")");
    }

    private void registerCustomer(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: registerCustomer <firstName> <surname> <username> <address> <password>");
            return;
        }

        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String address = args[3];
        String password = args[4];

        if (supermarket.userExists(username)) {
            System.out.println("Username already exists: " + username);
            return;
        }

        supermarket.addCustomer(username, firstName, surname, address, password);
        System.out.println("Registered customer: " + firstName + " " + surname + " (" + username + ")");
    }

    private void setCategoryDiscount(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: setCategoryDiscount <categoryName> <discountPercentage>");
            return;
        }

        if (session.getCurrentUser() == null || !session.getCurrentUser().getRole().equals("Manager")) {
            System.out.println("Only managers can set category discounts.");
            return;
        }

        String categoryName = args[0];
        double discountPercentage;

        try {
            discountPercentage = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid discount percentage: " + args[1]);
            return;
        }

        if (discountPercentage < 0 || discountPercentage > 100) {
            System.out.println("Discount percentage must be between 0 and 100.");
            return;
        }

        Category category = supermarket.getCategoryOrCreate(categoryName);
        category.categoryDiscountPercentage = discountPercentage;
        System.out.println("Set discount for category " + categoryName + " to " + discountPercentage + "%");
    }

    private void addItem(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: addItem <name> <category> <unitPrice> <weight> <stock>");
            return;
        }

        if (session.getCurrentUser() == null || !session.getCurrentUser().getRole().equals("Manager")) {
            System.out.println("Only managers can add items.");
            return;
        }

        String categoryName = args[1];
        String itemName = args[0];
        double weight;
        double price;
        Integer stock;

        try {
            price = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price: " + args[2]);
            return;
        }

        try {
            weight = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid weight: " + args[3]);
            return;
        }

        try {
            stock = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock: " + args[4]);
            return;
        }

        supermarket.addItem(categoryName, new catalog.Item(itemName, price, weight, stock));
        System.out.println("Added item " + itemName + " to category " + categoryName + " with price " + price + ", " + weight + " and stock " + stock);
    }

    private void login(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: login <username> <password>");
            return;
        }

        String name = args[0];
        String password = args[1];

        if (!supermarket.userExists(name)) {
            System.out.println("User not found: " + name);
            return;
        }

        User user = supermarket.getUser(name);

        if (user.checkPassword(password)) {
            session.login(user);
            System.out.println("Logged in as " + user.getFirstName() + " (" + user.getRole() + ")");
        } else {
            System.out.println("Incorrect password for user: " + name);
        }        
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help - Show this help message");
        System.out.println("  login <username> <password> - Log in as a user");
        System.out.println("  addItem <name> <category> <unitPrice> <weight> <stock> - Add an item to a category (Manager only)");
        System.out.println("  setCategoryDiscount <categoryName> <discountPercentage> - Set discount for a category (Manager only)");
        System.out.println("  registerCashier <firstName> <surname> <username> <password> - Register a cashier");
        System.out.println("  registerCustomer <firstName> <surname> <username> <address> <password> - Register a customer");
        System.out.println("  registerManager <firstName> <surname> <username> <password> - Register a manager");
        System.out.println("  runFile <filename> - Run commands from a file");
        System.out.println("  setup - Set up the supermarket with initial data");
        System.out.println("  subscribeToPlan <planName> - Subscribe to a discount plan (Customer only)");
        System.out.println("  requestDelivery - Request delivery for the current customer (Customer only)");
        System.out.println("  startCheckout <customerUsername> - Start checkout for a customer (Cashier only)");
        System.out.println("  scanItem <itemName> <quantity> - Scan an item during checkout (Cashier only)");
        System.out.println("  computeBill - Compute the total bill for the current checkout (Cashier only)");
        System.out.println("  stop | exit - Exit the CLI");
    }

    private String[] parseArguments(String input) {
        List<String> arguments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (Character.isWhitespace(c) && !insideQuotes) {
                if (current.length() > 0) {
                    arguments.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            arguments.add(current.toString());
        }

        return arguments.toArray(new String[0]);
    }
}
