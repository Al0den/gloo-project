package core;

import cli.CLI;

public class Main {
    private static final String CONFIG_FILE = "my_supermarket.ini";

    public static void main(String[] args) {
        Supermarket supermarket = new Supermarket();
        CLI cli = new CLI(supermarket);

        cli.setSilent(true); // Silent to not get intial setup spam
        try {
            cli.runFile(CONFIG_FILE);
            System.out.println("Loaded configuration from " + CONFIG_FILE);
        } catch (Exception e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
        cli.setSilent(false);

        cli.run();
    }
}
