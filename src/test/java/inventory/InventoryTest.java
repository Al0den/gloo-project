package inventory;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class InventoryTest {
    @Test
    void categoryAddRemove() {
        Inventory inventory = new Inventory(new HashMap<>());

        inventory.addCategory("dairy");

        assertSame(inventory.getCategories().get("dairy"), inventory.getCategory("dairy"));

        inventory.removeCategory("dairy");

        assertNull(inventory.getCategory("dairy"));
    }

    @Test
    void addItem() {
        Inventory inventory = new Inventory(new HashMap<>());
        inventory.addCategory("dairy");
        Item milk = new Item("milk", 1.5, 1.0, 10);

        inventory.addItem("dairy", milk);

        assertSame(milk, inventory.getItem("milk"));
        assertSame(milk, inventory.getItem("dairy", "milk"));
        assertSame(inventory.getCategory("dairy"), inventory.getItemCategory("milk"));
    }

    @Test
    void missingCategory() {
        Inventory inventory = new Inventory(new HashMap<>());
        Item milk = new Item("milk", 1.5, 1.0, 10);

        inventory.addItem("missing", milk);

        assertNull(inventory.getItem("milk"));
    }
}
