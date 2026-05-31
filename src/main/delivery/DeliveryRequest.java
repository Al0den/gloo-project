package delivery;

public class DeliveryRequest {
    private String address;
    private Double distanceKm;
    //private DeliverySlot slot;

    public DeliveryRequest(String address, Double distanceKm) {
        this.address = address;
        this.distanceKm = distanceKm;
    }

    public String getAddress() {
        return address;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }
}
