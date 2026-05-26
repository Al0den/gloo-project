package core;

public class Main {
    public static void main(String[] args) {
        Supermarket supermarket = new Supermarket();
        CLI cli = new CLI(supermarket);
        
        cli.run();
    }
}
