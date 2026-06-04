package inventory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class StockUnderThresholdTest {
    @Test
    void lowStockWarning() {
        Item item = new Item("milk", 1.5, 1.0, 4);
        StockUnderThreshold observer = new StockUnderThreshold();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output));
            observer.onStockUpdate(item);
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(output.toString().contains("Warning: Stock for item milk is under threshold"));
    }
}
