package discount;

public class NormalDiscountPolicy implements DiscountPolicy {
    private static int ONE_TIME_FEE = 0;
    @Override
    public double apply(double total) {
        return total;
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return deliveryFee;
    }

    @Override
    public double getOneTimeFee() {
        return ONE_TIME_FEE;
    }
}
