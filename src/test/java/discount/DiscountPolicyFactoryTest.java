package discount;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DiscountPolicyFactoryTest {
    @Test
    void knownPlans() {
        assertInstanceOf(NormalDiscountPolicy.class, DiscountPolicyFactory.create(null));
        assertInstanceOf(NormalDiscountPolicy.class, DiscountPolicyFactory.create("normal"));
        assertInstanceOf(PrimeDiscountPolicy.class, DiscountPolicyFactory.create("prime"));
        assertInstanceOf(PlatinumDiscountPolicy.class, DiscountPolicyFactory.create("platinum"));
    }

    @Test
    void caseInsensitive() {
        assertInstanceOf(PrimeDiscountPolicy.class, DiscountPolicyFactory.create("PRIME"));
    }

    @Test
    void unknownPlan() {
        assertThrows(IllegalArgumentException.class, () -> DiscountPolicyFactory.create("gold"));
    }
}
