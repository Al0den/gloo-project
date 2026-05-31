package delivery;

import users.Cart;

public class WeightDistanceDeliveryFee implements DeliveryFeePolicy {
    private static double PEAK_HOUR_COEFF = 1.3;
    private static double ECO_FRIENDLY_COEFF = 0.9;
    @Override
    public double computeFee(Cart cart, double itemsTotal, DeliveryRequest request) {
        double weight = cart.getTotalWeight();
        double distance = request.getDistanceKm();

        if (weight >= 50.0) {
            throw new IllegalStateException("Cannot deliver items heavier than 50kg");
        }

        if (weight <= 10.0 && distance <= 30) {
            return 15.0;
        }

        double fee = 15.0 + 0.05 * itemsTotal;

        DeliverySlot slot = request.getSlot();
        if (slot.isPeakHour()) {
            fee *= PEAK_HOUR_COEFF;
        }
        if (slot.isEcoFriendly()) {
            fee *= ECO_FRIENDLY_COEFF;
        }

        return fee;
    }
}
