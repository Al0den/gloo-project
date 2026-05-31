package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import users.Cart;

import org.junit.jupiter.api.Test;

class DeliveryFeePolicyTest {
    @Test
    void deliveryFeePolicyCanBeImplementedByAnotherClass() {
        DeliveryFeePolicy policy = new FixedFeePolicy();

        assertEquals(7.0, policy.computeFee(new Cart(), 100.0, new DeliveryRequest("1 Main St", 1.0)));
    }

    private static class FixedFeePolicy implements DeliveryFeePolicy {
        @Override
        public double computeFee(Cart cart, double itemsTotal, DeliveryRequest request) {
            return 7.0;
        }
    }
}
