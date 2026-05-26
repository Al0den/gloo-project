package catalog;

import java.util.Map;

public class Catalog {
    private Map<String, Category> categories;

    public Catalog(Map<String, Category> categories) {
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
