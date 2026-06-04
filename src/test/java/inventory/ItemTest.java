package inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class ItemTest {
    private static final double EPSILON = 0.0001;

    @Test
    void constructorRejectsInvalidItemInformation() {
        assertThrows(IllegalArgumentException.class, () -> new Item(null, 1.5, 1.0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Item("", 1.5, 1.0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Item("milk", -0.01, 1.0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Item("milk", 1.5, -0.01, 10));
        assertThrows(IllegalArgumentException.class, () -> new Item("milk", 1.5, 1.0, null));
        assertThrows(IllegalArgumentException.class, () -> new Item("milk", 1.5, 1.0, -1));
    }

    @Test
    void setPriceChangesPrice() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        item.setPrice(2.0);

        assertEquals(2.0, item.getPrice(), EPSILON);
    }

    @Test
    void setPriceRejectsNegativePrice() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        assertThrows(IllegalArgumentException.class, () -> item.setPrice(-0.01));
        assertEquals(1.5, item.getPrice(), EPSILON);
    }

    @Test
    void setStockRejectsNegativeStock() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        assertThrows(IllegalArgumentException.class, () -> item.setStock(-1));
        assertEquals(10, item.getStock());
    }

    @Test
    void decreaseStockReducesStock() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        item.decreaseStock(3);

        assertEquals(7, item.getStock());
    }

    @Test
    void decreaseStockRejectsTooLargeAmount() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        assertThrows(IllegalArgumentException.class, () -> item.decreaseStock(11));
        assertEquals(10, item.getStock());
    }

    @Test
    void decreaseStockRejectsNegativeAmount() {
        Item item = new Item("milk", 1.5, 1.0, 10);

        assertThrows(IllegalArgumentException.class, () -> item.decreaseStock(-1));
        assertEquals(10, item.getStock());
    }

    @Test
    void customObserverIsNotifiedWhenStockChanges() {
        Item item = new Item("milk", 1.5, 1.0, 10);
        CountingObserver observer = new CountingObserver();
        item.addStockObserver(observer);

        runWithoutConsoleOutput(() -> item.setStock(4));

        assertEquals(1, observer.updateCount);
        assertEquals(item, observer.lastItem);
    }

    @Test
    void removedObserverIsNotNotifiedAnymore() {
        Item item = new Item("milk", 1.5, 1.0, 10);
        CountingObserver observer = new CountingObserver();
        item.addStockObserver(observer);
        item.removeStockObserver(observer);

        runWithoutConsoleOutput(() -> item.setStock(4));

        assertEquals(0, observer.updateCount);
    }

    private static void runWithoutConsoleOutput(Runnable action) {
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            action.run();
        } finally {
            System.setOut(originalOut);
        }
    }

    private static class CountingObserver implements StockObserver {
        private int updateCount = 0;
        private Item lastItem;

        @Override
        public void onStockUpdate(Item item) {
            updateCount++;
            lastItem = item;
        }
    }
}
