package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import discount.NormalDiscountPolicy;
import discount.PlatinumDiscountPolicy;

import org.junit.jupiter.api.Test;

class CustomerTest {
    @Test
    void customerHasCustomerRoleAndInitialPlan() {
        NormalDiscountPolicy plan = new NormalDiscountPolicy();
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", plan);

        assertEquals("Customer", customer.getRole());
        assertSame(plan, customer.getDiscountPolicy());
    }

    @Test
    void customerCanChangeDiscountPlan() {
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", new NormalDiscountPolicy());
        PlatinumDiscountPolicy newPlan = new PlatinumDiscountPolicy();

        customer.setDiscountPolicy(newPlan);

        assertSame(newPlan, customer.getDiscountPolicy());
    }

    @Test
    void requestDeliveryStoresDeliveryInformation() {
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", new NormalDiscountPolicy());

        assertFalse(customer.hasRequestedDelivery());

        customer.requestDelivery("2 Delivery St", 12.5);

        assertTrue(customer.hasRequestedDelivery());
        assertEquals("2 Delivery St", customer.getDeliveryRequest().getAddress());
        assertEquals(12.5, customer.getDeliveryRequest().getDistanceKm());
    }
}
