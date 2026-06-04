package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import users.Cart;

import org.junit.jupiter.api.Test;

class DeliveryFeePolicyTest {
    @Test
    void policyImpl() {
        DeliveryFeePolicy policy = new FixedFeePolicy();
        DeliverySlot slot = new DeliverySlot("morning", 8, 12, 100.0, false, false);

        assertEquals(7.0, policy.computeFee(new Cart(), 100.0, new DeliveryRequest("1 Main St", 1.0, slot)));
    }

    private static class FixedFeePolicy implements DeliveryFeePolicy {
        @Override
        public double computeFee(Cart cart, double itemsTotal, DeliveryRequest request) {
            return 7.0;
        }
    }
}
