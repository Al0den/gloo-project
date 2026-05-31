package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeliverySlotTest {
    @Test
    void slotStoresTimeWindowFlagsAndBookedWeight() {
        DeliverySlot slot = new DeliverySlot("lunch", 12, 14, 100.0, true, false);

        assertEquals("lunch", slot.getSlotId());
        assertEquals(12, slot.getStartHour());
        assertEquals(14, slot.getEndHour());
        assertTrue(slot.isPeakHour());
        assertFalse(slot.isEcoFriendly());
        assertEquals(0.0, slot.getBookedWeightKg());
    }

    @Test
    void bookAddsWeightUntilCapacityIsExceeded() {
        DeliverySlot slot = new DeliverySlot("morning", 8, 12, 10.0, false, false);

        slot.book(6.0);

        assertTrue(slot.canAccept(4.0));
        assertFalse(slot.canAccept(4.1));
        assertEquals(6.0, slot.getBookedWeightKg());
        assertThrows(IllegalStateException.class, () -> slot.book(4.1));
    }
}
