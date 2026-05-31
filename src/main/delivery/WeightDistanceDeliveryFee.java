package delivery;

import users.Cart;

public class WeightDistanceDeliveryFee implements DeliveryFeePolicy {
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

        return 15.0 + 0.05 * itemsTotal;
    }
}
