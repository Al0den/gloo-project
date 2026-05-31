package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import inventory.Item;
import inventory.NormalCategoryPricingPolicy;

import org.junit.jupiter.api.Test;

class NormalCategoryPricingPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void normalCategoryPolicyMultipliesUnitPriceByQuantity() {
        NormalCategoryPricingPolicy policy = new NormalCategoryPricingPolicy();
        Item item = new Item("milk", 2.0, 1.0, 10);

        assertEquals(6.0, policy.apply(item, 3), EPSILON);
    }
}
