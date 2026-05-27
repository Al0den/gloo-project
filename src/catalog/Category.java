package catalog;

import java.util.Map;
import java.util.HashMap;

public class Category {
    private Map<String, Item> items;
    public double categoryDiscountPercentage = 0.0;

    public Category() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public void removeItem(String itemName) {
        items.remove(itemName);
    }

    public void setPrice(String itemName, double newPrice) {
        Item item = items.get(itemName);
        if (item != null) {
            item.setPrice(newPrice);
        }
    }

    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    public Map<String, Item> getItems() {
        return new HashMap<>(items);
    }

    public Category getCategory() {
        return this;
    }

}
