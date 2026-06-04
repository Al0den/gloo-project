package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NormalDiscountPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void normalPlan() {
        NormalDiscountPolicy policy = new NormalDiscountPolicy();

        assertEquals(100.0, policy.apply(100.0), EPSILON);
        assertEquals(15.0, policy.applyDeliveryDiscount(15.0), EPSILON);
    }
}
