package inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class CategoryTest {
    private static final double EPSILON = 0.0001;

    @Test
    void addAndRemoveItemUpdatesCategoryItems() {
        Category category = new Category();
        Item milk = new Item("milk", 1.5, 1.0, 10);

        category.addItem(milk);

        assertSame(milk, category.getItem("milk"));

        category.removeItem("milk");

        assertNull(category.getItem("milk"));
    }

    @Test
    void setPriceChangesItemPrice() {
        Category category = new Category();
        Item milk = new Item("milk", 1.5, 1.0, 10);
        category.addItem(milk);

        category.setPrice("milk", 2.0);

        assertEquals(2.0, milk.getPrice(), EPSILON);
    }

    @Test
    void categoryCanUseCustomPricingPolicy() {
        Category category = new Category();
        Item apple = new Item("apple", 1.0, 0.2, 10);

        category.setPricingPolicy(new PercentageCategoryPricingPolicy(10.0));

        assertEquals(4.5, category.getPricingPolicy().apply(apple, 5), EPSILON);
    }
}
