package discount;

public class PrimeDiscountPolicy implements DiscountPolicy {
    private static double DISCOUNT_RATE = 0.20;
    private static double MINIMUM_AMOUNT = 50.0;

    private static double DELIVERY_DISCOUNT_RATE = 0.50;

    @Override
    public double apply(double total) {
        if (total >= MINIMUM_AMOUNT) {
            return total * (1 - DISCOUNT_RATE);
        }
        return total;
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return deliveryFee * (1 - DELIVERY_DISCOUNT_RATE);
    }

    
}
