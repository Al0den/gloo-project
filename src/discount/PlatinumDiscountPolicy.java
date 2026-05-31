package discount;

public class PlatinumDiscountPolicy implements DiscountPolicy {
    private static double DISCOUNT_RATE = 0.30;
    private static double DELIVERY_DISCOUNT_RATE = 1.00;

    @Override
    public double apply(double total) {
        return total * (1 - DISCOUNT_RATE);
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return deliveryFee * (1 - DELIVERY_DISCOUNT_RATE);
    }
}
