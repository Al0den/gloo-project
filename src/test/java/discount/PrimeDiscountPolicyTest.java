package discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrimeDiscountPolicyTest {
    private static final double EPSILON = 0.0001;

    @Test
    void primeDiscountAppliesOnlyFromFiftyEuros() {
        PrimeDiscountPolicy policy = new PrimeDiscountPolicy();

        assertEquals(49.99, policy.apply(49.99), EPSILON);
        assertEquals(40.0, policy.apply(50.0), EPSILON);
        assertEquals(80.0, policy.apply(100.0), EPSILON);
    }

    @Test
    void primeCustomerPaysHalfDeliveryFee() {
        PrimeDiscountPolicy policy = new PrimeDiscountPolicy();

        assertEquals(7.5, policy.applyDeliveryDiscount(15.0), EPSILON);
    }
}
