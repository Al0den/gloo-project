package delivery;

public class DeliverySlot {
    private String slotId;
    private int startHour;
    private int endHour;

    private double maxCapacityKg;
    private double bookedWeightKg;
    private boolean peakHour;
    private boolean ecoFriendly;

    public DeliverySlot(String slotId, int startHour, int endHour, double maxCapacityKg, boolean peakHour, boolean ecoFriendly) {
        this.slotId = slotId;
        this.startHour = startHour;
        this.endHour = endHour;
        this.maxCapacityKg = maxCapacityKg;
        this.peakHour = peakHour;
        this.ecoFriendly = ecoFriendly;
        this.bookedWeightKg = 0.0;
    }

    public boolean canAccept(double weightKg) {
        return bookedWeightKg + weightKg <= maxCapacityKg;
    }

    public void book(double weightKg) {
        if (canAccept(weightKg)) {
            bookedWeightKg += weightKg;
        } else {
            throw new IllegalStateException("Delivery slot capacity exceeded");
        }
    }

    public String getSlotId() {
        return slotId;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public boolean isPeakHour() {
        return peakHour;
    }

    public boolean isEcoFriendly() {
        return ecoFriendly;
    }

    public double getBookedWeightKg() {
        return bookedWeightKg;
    }
}
