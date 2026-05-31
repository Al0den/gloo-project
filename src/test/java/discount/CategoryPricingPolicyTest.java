package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import inventory.CategoryPricingPolicy;
import inventory.Item;

import org.junit.jupiter.api.Test;

class CategoryPricingPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void categoryPricingPolicyCanBeImplementedByAnotherClass() {
        CategoryPricingPolicy policy = new FixedCategoryPricingPolicy();

        assertEquals(3.0, policy.apply(new Item("milk", 2.0, 1.0, 10), 5), EPSILON);
    }

    private static class FixedCategoryPricingPolicy implements CategoryPricingPolicy {
        @Override
        public double apply(Item item, int quantity) {
            return 3.0;
        }
    }
}
