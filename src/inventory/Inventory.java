package inventory;

import java.util.Map;

public class Inventory {
    private Map<String, Category> categories;

    public Inventory(Map<String, Category> categories) {
        this.categories = categories;
    }

    public void addCategory(String categoryName) {
        categories.put(categoryName, new Category());
    }

    public void removeCategory(String categoryName) {
        categories.remove(categoryName);
    }

    public void addItem(String categoryName, Item item) {
        Category category = categories.get(categoryName);
        if (category != null) {
            category.addItem(item);
        }
    }

    public Item getItem(String itemName) {
        for (Category category : categories.values()) {
            Item item = category.getItem(itemName);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public Category getItemCategory(String itemName) {
        for (Map.Entry<String, Category> entry : categories.entrySet()) {
            Category category = entry.getValue();
            if (category.getItem(itemName) != null) {
                return category;
            }
        }
        return null; 
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    public Item getItem(String categoryName, String itemName) {
        Category category = categories.get(categoryName);
        if (category != null) {
            return category.getItem(itemName);
        }
        return null;
    }

    public Category getCategory(String categoryName) {
        return categories.get(categoryName);
    }
}
