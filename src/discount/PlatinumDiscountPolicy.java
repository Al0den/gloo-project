package discount;

public class PlatinumDiscountPolicy implements DiscountPolicy {
    private static double DISCOUNT_RATE = 0.30;

    @Override
    public double apply(double total) {
        return total * (1 - DISCOUNT_RATE);
    }
}
