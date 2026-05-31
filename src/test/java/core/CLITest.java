package core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import cli.CLI;

class CLITest {
    @Test
    void runDisplaysHelpAndThenExits() {
        String output = runCliWithInput("help\nexit\n");

        assertTrue(output.contains("Welcome to the Supermarket CLI"));
        assertTrue(output.contains("login <username> <password>"));
        assertTrue(output.contains("restock <itemName> <quantity>"));
        assertTrue(output.contains("Exiting..."));
    }

    @Test
    void managerCanRestockItemFromCli() {
        String output = runCliWithInput(
                "login ceo 123456789\n"
                + "additem milk dairy 1.20 1.00 50\n"
                + "restock milk 25\n"
                + "showinventory\n"
                + "exit\n"
        );

        assertTrue(output.contains("Restocked item milk by 25. Current stock: 75"));
        assertTrue(output.contains("milk - Price: 1.2, Stock: 75"));
    }

    @Test
    void showInventoryMarksLowStockItems() {
        String output = runCliWithInput(
                "login ceo 123456789\n"
                + "additem steak meat 12.50 0.50 4\n"
                + "additem milk dairy 1.20 1.00 50\n"
                + "showinventory\n"
                + "exit\n"
        );

        assertTrue(output.contains("steak - Price: 12.5, Stock: 4 [LOW STOCK]"));
        assertTrue(output.contains("milk - Price: 1.2, Stock: 50"));
    }

    @Test
    void requestDeliveryUsesLevenshteinDistanceFromSupermarketAddress() {
        String output = runCliWithInput(
                "login ceo 123456789\n"
                + "additem basket dairy 100.00 1.00 10\n"
                + "registercustomer Alice Martin alice \"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ\" alicepwd\n"
                + "logout\n"
                + "login alice alicepwd\n"
                + "requestdelivery \"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ\"\n"
                + "logout\n"
                + "login ceo 123456789\n"
                + "registercashier Bob Dupont bob bobpwd\n"
                + "logout\n"
                + "login bob bobpwd\n"
                + "startcheckout alice\n"
                + "scanitem basket 1\n"
                + "computebill\n"
                + "exit\n"
        );

        assertTrue(output.contains("Total bill: 120.0"));
    }

    private static String runCliWithInput(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(output));
            new CLI(new Supermarket()).run();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return output.toString();
    }
}
