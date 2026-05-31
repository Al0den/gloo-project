package discount;

public interface DiscountPolicy {
    double getOneTimeFee();
    double apply(double total);
    double applyDeliveryDiscount(double deliveryFee);


}