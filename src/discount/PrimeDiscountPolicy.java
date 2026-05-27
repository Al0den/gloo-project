package discount;

public class PrimeDiscountPolicy implements DiscountPolicy {
    private static double DISCOUNT_RATE = 0.20;
    private static double MINIMUM_AMOUNT = 50.0;

    @Override
    public double apply(double total) {
        if (total >= MINIMUM_AMOUNT) {
            return total * (1 - DISCOUNT_RATE);
        }
        return total;
    }
    
}
