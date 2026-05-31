package cli;

import users.User;
import users.Customer;
import users.Cart;
import inventory.Category;
import inventory.Item;
import core.Supermarket;
import delivery.DistanceCalculator;
import delivery.LevenschteinDistanceCalculator;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CLI {
    private static final String SUPERMARKET_ADDRESS = "14 Mail Pierre Potier, Gif sur Yvette";

    private Map<String, CommandInfo> commands;
    private Supermarket supermarket;
    private Session session;
    private DistanceCalculator distanceCalculator;

    public CLI(Supermarket supermarket) {
        this.supermarket = supermarket;
        this.session = new Session();
        this.commands = new HashMap<>();
        this.distanceCalculator = new LevenschteinDistanceCalculator();

        registerCommands();
    }

    private void registerCommands() {
        commands.put("help", new CommandInfo("Display help information", this::printHelp, null, 0, "help"));
        commands.put("login", new CommandInfo("Login to the system", this::login, null, 2, "login <username> <password>"));
        commands.put("logout", new CommandInfo("Logout from the system", this::logout, null, 0, "logout"));
        commands.put("additem", new CommandInfo("Add an item to the inventory", this::addItem, "Manager", 5, "additem <name> <category> <unitPrice> <weight> <stock>"));
        commands.put("restock", new CommandInfo("Restock an inventory item", this::restock, "Manager", 2, "restock <itemName> <quantity>"));
        commands.put("setcategorydiscount", new CommandInfo("Set discount for a category", this::setCategoryDiscount, "Manager", 2, "setcategorydiscount <category> <discount>"));
        commands.put("registercustomer", new CommandInfo("Register a new customer", this::registerCustomer, "Manager", 5, "registercustomer <firstName> <surname> <username> <address> <password>"));
        commands.put("registermanager", new CommandInfo("Register a new manager", this::registerManager, "Manager", 4, "registermanager <firstName> <surname> <username> <password>"));
        commands.put("registercashier", new CommandInfo("Register a new cashier", this::registerCashier, "Manager", 4, "registercashier <firstName> <surname> <username> <password>"));
        commands.put("runtest", new CommandInfo("Run a script file", this::runFile, null, 1, "runtest <filename>"));
        commands.put("setup", new CommandInfo("Initialize the system", this::setup, "Manager", 0, "setup"));
        commands.put("subscribetoplan", new CommandInfo("Subscribe to a plan", this::subscribeToPlan, "Customer", 1, "subscribetoplan <plan_id>"));
        commands.put("requestdelivery", new CommandInfo("Request delivery", this::requestDelivery, "Customer", 1, "requestdelivery <address>"));
        commands.put("startcheckout", new CommandInfo("Start a checkout session", this::startCheckout, "Cashier", 1, "startcheckout <customerUsername>"));
        commands.put("scanitem", new CommandInfo("Scan an item", this::scanItem, "Cashier", 2, "scanitem <item_id> <quantity>"));
        commands.put("computebill", new CommandInfo("Compute the bill", this::computeBill, "Cashier", 0, "computebill"));
        commands.put("simulatepayment", new CommandInfo("Force next payment outcome", this::simulatePayment, "Cashier", 1, "simulatepayment <SUCCESS|INSUFFICIENT_FUNDS|PIN_WRONG|AUTH_DENIED>"));
        commands.put("pay", new CommandInfo("Pay the bill", this::pay, "Cashier", 2, "pay <cardNumber> <pin>"));
        commands.put("showrevenue", new CommandInfo("Show revenue", this::showRevenue, "Manager", 0, "showrevenue"));
        commands.put("showinventory", new CommandInfo("Show inventory", this::showInventory, "Manager", 0, "showinventory"));
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

        
        CommandInfo cmdInfo = commands.get(command);
        if (cmdInfo != null) {
            Boolean hasPermission = checkPermission(session, cmdInfo.getRequiredRole(), "execute this command");
            if (!hasPermission) return;

            Boolean validArgs = checkMinArgsLength(args, cmdInfo.getMinArgs(), cmdInfo.getUsage());
            if (!validArgs) return;

            try {
                cmdInfo.getAction().execute(args);
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    private void logout(String[] args) {
        session.logout();
        System.out.println("Logged out.");
    }

    private void pay(String[] args) {
        if (!session.hasActiveCheckout()) {
            System.out.println("No active checkout.");
            return;
        }

        if (!session.hasComputedBill()) {
            System.out.println("Compute the bill before payment.");
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

    private void setup(String[] args) {
        supermarket.setup();
        System.out.println("Supermarket setup completed.");
    }

    private void showRevenue(String[] args) {
        System.out.println("Total revenue: " + supermarket.getRevenue());
    }

    private void showInventory(String[] args) {
        System.out.println("Current inventory:");
        for (Map.Entry<String, Item> entry : supermarket.getInventory().entrySet()) {
            String itemName = entry.getKey();
            Item item = entry.getValue();
            String lowStockMarker = item.getStock() < item.getLowStockThreshold() ? " [LOW STOCK]" : "";
            System.out.println(itemName + " - Price: " + item.getPrice() + ", Stock: " + item.getStock() + lowStockMarker);
        }
    }

    private void simulatePayment(String[] args) {
        try {
            payment.PaymentOutcome outcome = payment.PaymentOutcome.valueOf(args[0].toUpperCase());
            supermarket.getPosDevice().simulateNextPayment(outcome);
            System.out.println("Next payment forced to: " + outcome);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid payment outcome: " + args[0]);
        }
    }

    private void computeBill(String[] args) {
        if (!session.hasActiveCheckout()) {
            System.out.println("No active checkout. Use startCheckout to begin.");
            return;
        }
        
        Cart cart = session.getCurrentCart();
        Customer customer = session.getCheckoutCustomer();

        double total = supermarket.computeBill(customer, cart);

        session.setCurrentBill(total);
        System.out.println("Total bill: " + total);
    }

    private void scanItem(String[] args) {
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

        Integer quantity = parseIntArg(args[1], "quantity");
        if (quantity == null) return;

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        Integer stock = item.getStock();
        if (stock != null && stock < quantity) {
            System.out.println("Insufficient stock for item: " + item.getName() + ". Available: " + stock + ", requested: " + quantity);
            return;
        }

        session.getCurrentCart().addItem(item, cat, quantity);
        System.out.println("Scanned item: " + item.getName() + " - Price: " + item.getPrice() * quantity);
    }

    private void startCheckout(String[] args) {
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
        Customer customer = (Customer) session.getCurrentUser();
        String address = args[0];
        double distance = distanceCalculator.calculateDistance(address, SUPERMARKET_ADDRESS);

        customer.requestDelivery(address, distance);
        System.out.println("Delivery requested to address: " + address + ", distance: " + distance + " km");
    }

    private void subscribeToPlan(String[] args) {
        String planName = args[0];
        Customer customer = (Customer) session.getCurrentUser();

        supermarket.subscribeToPlan(customer, planName);
        System.out.println("Subscribed to discount plan: " + planName);
    }

    private void runFile(String[] args) {
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
        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String password = args[3];

        if (supermarket.userExists(username)) {
            System.out.println("Username already exists: " + username);
            return;
        }

        supermarket.registerManager(firstName, surname, username, password);
        System.out.println("Registered manager: " + firstName + " " + surname + " (" + username + ")");
    }

    private void registerCashier(String[] args) {
        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String password = args[3];

        if (supermarket.userExists(username)) {
            System.out.println("Username already exists: " + username);
            return;
        }

        supermarket.registerCashier(firstName, surname, username, password);
        System.out.println("Registered cashier: " + firstName + " " + surname + " (" + username + ")");
    }

    private void registerCustomer(String[] args) {
        String firstName = args[0];
        String surname = args[1];
        String username = args[2];
        String address = args[3];
        String password = args[4];

        supermarket.registerCustomer(firstName, surname, username, address, password, "normal");
        System.out.println("Registered customer: " + firstName + " " + surname + " (" + username + ")");
    }

    private void setCategoryDiscount(String[] args) {
        String categoryName = args[0];
        
        Double discountPercentage = parseDoubleArg(args[1], "discountPercentage");
        if (discountPercentage == null) return;
        
        supermarket.setCategoryDiscount(categoryName, discountPercentage);
    }

    private void addItem(String[] args) {
        String categoryName = args[1];
        String itemName = args[0];

        Double price = parseDoubleArg(args[2], "unitPrice");
        if (price == null) return;

        Double weight = parseDoubleArg(args[3], "weight");
        if (weight == null) return;

        Integer stock = parseIntArg(args[4], "stock");
        if (stock == null) return;

        supermarket.addItem(categoryName, itemName, price, weight, stock);
        System.out.println("Added item " + itemName + " to category " + categoryName + " with price " + price + ", " + weight + " and stock " + stock);
    }

    private void restock(String[] args) {
        String itemName = args[0];

        Integer quantity = parseIntArg(args[1], "quantity");
        if (quantity == null) return;

        supermarket.restock(itemName, quantity);
        System.out.println("Restocked item " + itemName + " by " + quantity + ". Current stock: " + supermarket.getItem(itemName).getStock());
    }

    private void login(String[] args) {
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

    private void printHelp(String[] args) {
        for (Map.Entry<String, CommandInfo> entry : commands.entrySet()) {
            CommandInfo cmdInfo = entry.getValue();
            System.out.println(cmdInfo.getUsage() + " - " + cmdInfo.getDescription());
        }
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

    private static Double parseDoubleArg(String value, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid " + fieldName + ": " + value);
            return null;
        }
    }

    private static Integer parseIntArg(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid " + fieldName + ": " + value);
            return null;
        }
    }

    private static Boolean checkMinArgsLength(String[] args, Integer minLength, String usage) {
        if (minLength != null && args.length < minLength) {
            System.out.println("Usage: " + usage);
            return false;
        }
        return true;
    }

    private static Boolean checkPermission(Session session, String requiredRole, String actionDescription) {
        if (requiredRole == null) {
            return true;
        }
        if (session.getCurrentUser() == null || !session.getCurrentUser().getRole().equals(requiredRole)) {
            System.out.println("Only " + requiredRole.toLowerCase() + "s can " + actionDescription + ".");
            return false;
        }
        return true;
    }
}
