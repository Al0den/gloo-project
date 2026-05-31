package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DiscountPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void discountPolicyCanBeImplementedByAnotherClass() {
        DiscountPolicy policy = new FixedDiscountPolicy();

        assertEquals(90.0, policy.apply(100.0), EPSILON);
        assertEquals(5.0, policy.applyDeliveryDiscount(10.0), EPSILON);
    }

    private static class FixedDiscountPolicy implements DiscountPolicy {
        @Override
        public double apply(double total) {
            return total - 10.0;
        }

        @Override
        public double applyDeliveryDiscount(double deliveryFee) {
            return deliveryFee / 2.0;
        }

        @Override
        public double getOneTimeFee() {
            return 0.0;
        };
    }
}
