package delivery;

import java.util.HashMap;
import java.util.Map;

public class DeliveryScheduler {
    private Map<String, DeliverySlot> slots = new HashMap<>();

    public DeliveryScheduler() {
            slots.put("morning", new DeliverySlot("morning", 8, 12, 100.0, false, false));
            slots.put("lunch", new DeliverySlot("lunch", 12, 14, 100.0, true, false));
            slots.put("afternoon", new DeliverySlot("afternoon", 14, 18, 100.0, false, true));
            slots.put("evening", new DeliverySlot("evening", 18, 20, 100.0, true, false));
    }

    public DeliverySlot getSlot(String slotId) {
        DeliverySlot slot = slots.get(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Unknown delivery slot: " + slotId);
        }
        return slot;
    }

    public void bookSlot(String slotId, double weightKg) {
        DeliverySlot slot = getSlot(slotId);
        slot.book(weightKg);
    }

    public Map<String, DeliverySlot> getSlots() {
        return new HashMap<>(slots);
    }
}
