package users;

import discount.DiscountPolicy;
import inventory.Category;
import inventory.Item;

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

    public double getTotalPrice(DiscountPolicy discountPolicy) {
        double total = 0;

        for (Map.Entry<Category, Map<Item, Integer>> categoryEntry : items.entrySet()) {
            Category category = categoryEntry.getKey();
            Map<Item, Integer> itemMap = categoryEntry.getValue();

            for (Map.Entry<Item, Integer> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getKey();
                int quantity = itemEntry.getValue();

                double linePrice = category.getPricingPolicy().apply(item, quantity);
                total += linePrice;
            }
        }
        
        total = discountPolicy.apply(total);

        return total;
    }

    public void finalizeSale() {
        for (Map.Entry<Category, Map<Item, Integer>> categoryEntry : items.entrySet()) {
            Map<Item, Integer> itemMap = categoryEntry.getValue();

            for (Map.Entry<Item, Integer> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getKey();
                int quantity = itemEntry.getValue();
                item.decreaseStock(quantity);
            }
        }

        items = new HashMap<>();
    }

    public Double getTotalWeight() {
        double totalWeight = 0;

        for (Map.Entry<Category, Map<Item, Integer>> categoryEntry : items.entrySet()) {
            Map<Item, Integer> itemMap = categoryEntry.getValue();

            for (Map.Entry<Item, Integer> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getKey();
                int quantity = itemEntry.getValue();

                totalWeight += item.getWeight() * quantity;
            }
        }

        return totalWeight;
    }

}
