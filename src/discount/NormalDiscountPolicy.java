package discount;

public class NormalDiscountPolicy implements DiscountPolicy {
    @Override
    public double apply(double total) {
        return total;
    }
}
