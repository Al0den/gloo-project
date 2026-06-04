package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DeliveryRequestTest {
    @Test
    void requestData() {
        DeliverySlot slot = new DeliverySlot("morning", 8, 12, 100.0, false, false);

        DeliveryRequest request = new DeliveryRequest("1 Main St", 12.5, slot);

        assertEquals("1 Main St", request.getAddress());
        assertEquals(12.5, request.getDistanceKm());
        assertEquals(slot, request.getSlot());
    }
}
