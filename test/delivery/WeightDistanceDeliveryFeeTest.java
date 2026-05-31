package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import inventory.Category;
import inventory.Item;
import users.Cart;

import org.junit.jupiter.api.Test;

class WeightDistanceDeliveryFeeTest {
    private static final double EPSILON = 0.0001;

    @Test
    void computeFeeReturnsFixedFeeForLightNearbyDelivery() {
        Cart cart = new Cart();
        cart.addItem(new Item("milk", 2.0, 2.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 40.0, request);

        assertEquals(15.0, fee, EPSILON);
    }

    @Test
    void computeFeeAddsPercentageForHeavyOrFarDelivery() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 6.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request);

        assertEquals(20.0, fee, EPSILON);
    }

    @Test
    void computeFeeRejectsDeliveriesOfAtLeastFiftyKg() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 25.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0);

        assertThrows(
                IllegalStateException.class,
                () -> new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request)
        );
    }
}
