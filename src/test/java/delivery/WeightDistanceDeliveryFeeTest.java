package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import inventory.Category;
import inventory.Item;
import users.Cart;

import org.junit.jupiter.api.Test;

class WeightDistanceDeliveryFeeTest {
    private static final double EPSILON = 0.0001;
    private static final DeliverySlot NORMAL_SLOT = new DeliverySlot("morning", 8, 12, 100.0, false, false);

    @Test
    void fixedFee() {
        Cart cart = new Cart();
        cart.addItem(new Item("milk", 2.0, 2.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0, NORMAL_SLOT);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 40.0, request);

        assertEquals(15.0, fee, EPSILON);
    }

    @Test
    void heavyFee() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 6.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0, NORMAL_SLOT);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request);

        assertEquals(20.0, fee, EPSILON);
    }

    @Test
    void peakFee() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 6.0, 10), new Category(), 2);
        DeliverySlot peakSlot = new DeliverySlot("lunch", 12, 14, 100.0, true, false);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0, peakSlot);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request);

        assertEquals(26.0, fee, EPSILON);
    }

    @Test
    void ecoFee() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 6.0, 10), new Category(), 2);
        DeliverySlot ecoSlot = new DeliverySlot("afternoon", 14, 18, 100.0, false, true);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0, ecoSlot);

        double fee = new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request);

        assertEquals(18.0, fee, EPSILON);
    }

    @Test
    void tooHeavy() {
        Cart cart = new Cart();
        cart.addItem(new Item("water", 2.0, 25.0, 10), new Category(), 2);
        DeliveryRequest request = new DeliveryRequest("1 Main St", 10.0, NORMAL_SLOT);

        assertThrows(
                IllegalStateException.class,
                () -> new WeightDistanceDeliveryFee().computeFee(cart, 100.0, request)
        );
    }
}
