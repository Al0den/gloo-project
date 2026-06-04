package core;

import cli.CLI;

public class Main {
    private static final String CONFIG_FILE = "my_supermarket.ini";

    public static void main(String[] args) {
        Supermarket supermarket = new Supermarket();
        CLI cli = new CLI(supermarket);

        cli.setSilent(true); // Silent to not get intial setup spam
        cli.runFile(CONFIG_FILE);
        cli.setSilent(false);

        System.out.println("Loaded configuration from " + CONFIG_FILE);
        
        cli.run();
    }
}
