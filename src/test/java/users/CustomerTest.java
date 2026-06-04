package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import delivery.DeliverySlot;
import discount.NormalDiscountPolicy;
import discount.PlatinumDiscountPolicy;

import org.junit.jupiter.api.Test;

class CustomerTest {
    @Test
    void customerPlan() {
        NormalDiscountPolicy plan = new NormalDiscountPolicy();
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", plan);

        assertEquals("Customer", customer.getRole());
        assertSame(plan, customer.getDiscountPolicy());
    }

    @Test
    void changePlan() {
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", new NormalDiscountPolicy());
        PlatinumDiscountPolicy newPlan = new PlatinumDiscountPolicy();

        customer.setDiscountPolicy(newPlan);

        assertSame(newPlan, customer.getDiscountPolicy());
    }

    @Test
    void requestDelivery() {
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", new NormalDiscountPolicy());
        DeliverySlot slot = new DeliverySlot("morning", 8, 12, 100.0, false, false);

        assertFalse(customer.hasRequestedDelivery());

        customer.requestDelivery("2 Delivery St", 12.5, slot);

        assertTrue(customer.hasRequestedDelivery());
        assertEquals("2 Delivery St", customer.getDeliveryRequest().getAddress());
        assertEquals(12.5, customer.getDeliveryRequest().getDistanceKm());
        assertSame(slot, customer.getDeliveryRequest().getSlot());
    }

    @Test
    void clearDelivery() {
        Customer customer = new Customer("alice", "Alice", "Martin", "1 Main St", "pwd", new NormalDiscountPolicy());
        DeliverySlot slot = new DeliverySlot("morning", 8, 12, 100.0, false, false);
        customer.requestDelivery("2 Delivery St", 12.5, slot);

        customer.clearDeliveryRequest();

        assertFalse(customer.hasRequestedDelivery());
        assertNull(customer.getDeliveryRequest());
    }
}
