package users;

import catalog.Item;
import discount.DiscountPlan;

import catalog.Category;

import java.util.Map;
import java.util.HashMap;

public class Cart {
    protected Map<Category, Map<Item, Integer>> items; // Category -> (Item -> Quantity)

    @Override 
    public String toString() {
        String result = "Cart{";
        for (Map.Entry<Category, Map<Item, Integer>> categoryEntry : items.entrySet()) {
            Category category = categoryEntry.getKey();
            Map<Item, Integer> itemMap = categoryEntry.getValue();
            result += "\n  " + category + ":";
            for (Map.Entry<Item, Integer> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getKey();
                int quantity = itemEntry.getValue();
                result += "\n    " + item + " x " + quantity;
            }
        }
        result += "\n}";
        return result;
    }

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item, Category category, int quantity) {
        if (!items.containsKey(category)) {
            items.put(category, new HashMap<>());
        }

        Map<Item, Integer> itemMap = items.get(category);
        itemMap.put(item, itemMap.getOrDefault(item, 0) + quantity);
    }

    public double getTotalPrice(DiscountPlan discountPlan) {
        double total = 0;

        for (Map.Entry<Category, Map<Item, Integer>> categoryEntry : items.entrySet()) {
            Category category = categoryEntry.getKey();
            Map<Item, Integer> itemMap = categoryEntry.getValue();

            for (Map.Entry<Item, Integer> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getKey();
                int quantity = itemEntry.getValue();
                double price = item.getPrice() * quantity;

                price = discountPlan.applyCategoryDiscount(category, price);
                

                total += price;
            }
        }
        
        total = discountPlan.applyGlobalDiscount(total);

        return total;
    }
}
