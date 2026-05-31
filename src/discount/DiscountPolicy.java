package discount;

public interface DiscountPolicy {
    double apply(double total);
    double applyDeliveryDiscount(double deliveryFee);
}