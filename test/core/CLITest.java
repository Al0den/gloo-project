package core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class CLITest {
    @Test
    void runDisplaysHelpAndThenExits() {
        String output = runCliWithInput("help\nexit\n");

        assertTrue(output.contains("Welcome to the Supermarket CLI"));
        assertTrue(output.contains("login <username> <password>"));
        assertTrue(output.contains("Exiting..."));
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
