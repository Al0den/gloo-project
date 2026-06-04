package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlatinumDiscountPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void platinumBill() {
        PlatinumDiscountPolicy policy = new PlatinumDiscountPolicy();

        assertEquals(35.0, policy.apply(50.0), EPSILON);
        assertEquals(70.0, policy.apply(100.0), EPSILON);
    }

    @Test
    void freeDelivery() {
        PlatinumDiscountPolicy policy = new PlatinumDiscountPolicy();

        assertEquals(0.0, policy.applyDeliveryDiscount(15.0), EPSILON);
    }
}
