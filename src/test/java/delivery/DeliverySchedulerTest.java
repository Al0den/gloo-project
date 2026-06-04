package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeliverySchedulerTest {
    @Test
    void schedulerProvidesDefaultSlotsWithExpectedFlags() {
        DeliveryScheduler scheduler = new DeliveryScheduler();

        assertFalse(scheduler.getSlot("morning").isPeakHour());
        assertTrue(scheduler.getSlot("lunch").isPeakHour());
        assertTrue(scheduler.getSlot("afternoon").isEcoFriendly());
        assertTrue(scheduler.getSlot("evening").isPeakHour());
    }

    @Test
    void schedulerBooksSlotsAndRejectsUnknownSlot() {
        DeliveryScheduler scheduler = new DeliveryScheduler();

        scheduler.bookSlot("morning", 12.5);

        assertEquals(12.5, scheduler.getSlot("morning").getBookedWeightKg());
        assertThrows(IllegalArgumentException.class, () -> scheduler.getSlot("night"));
    }

    @Test
    void getSlotsReturnsCopyOfSlotMap() {
        DeliveryScheduler scheduler = new DeliveryScheduler();

        scheduler.getSlots().clear();

        assertEquals("morning", scheduler.getSlot("morning").getSlotId());
    }
}
