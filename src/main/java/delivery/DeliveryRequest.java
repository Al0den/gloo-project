package delivery;

public class DeliveryRequest {
    private String address;
    private Double distanceKm;
    private DeliverySlot slot;

    public DeliveryRequest(String address, Double distanceKm, DeliverySlot slot) {
        this.address = address;
        this.distanceKm = distanceKm;
        this.slot = slot;
    }

    public String getAddress() {
        return address;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public DeliverySlot getSlot() {
        return slot;
    }
}
