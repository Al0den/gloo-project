package delivery;

import users.Cart;

public interface DeliveryFeePolicy {
    public double computeFee(Cart cart, double itemsTotal, DeliveryRequest request);
}
