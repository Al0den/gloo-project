package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import inventory.Item;
import inventory.PercentageCategoryPricingPolicy;

import org.junit.jupiter.api.Test;

class PercentageCategoryPricingPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void percentagePolicyAppliesCategoryDiscount() {
        PercentageCategoryPricingPolicy policy = new PercentageCategoryPricingPolicy(10.0);
        Item item = new Item("apple", 1.0, 0.2, 10);

        assertEquals(4.5, policy.apply(item, 5), EPSILON);
    }

    @Test
    void percentagePolicyRejectsInvalidDiscounts() {
        assertThrows(IllegalArgumentException.class, () -> new PercentageCategoryPricingPolicy(-1.0));
        assertThrows(IllegalArgumentException.class, () -> new PercentageCategoryPricingPolicy(101.0));
    }
}
