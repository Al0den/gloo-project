package inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StockObserverTest {
    @Test
    void stockObserverCanReactToItemUpdates() {
        Item item = new Item("milk", 1.5, 1.0, 10);
        TestObserver observer = new TestObserver();

        observer.onStockUpdate(item);

        assertEquals("milk", observer.lastItemName);
    }

    private static class TestObserver implements StockObserver {
        private String lastItemName;

        @Override
        public void onStockUpdate(Item item) {
            lastItemName = item.getName();
        }
    }
}
