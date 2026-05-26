package core;

import catalog.Category;
import users.User;
import users.Customer;
import users.Manager;
import discount.DiscountPlan;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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
            default:
                System.out.println("Unknown command: " + command);
        }
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
