package discount;

public class DiscountPolicyFactory {
    public static DiscountPolicy create(String planName) {
        if (planName == null) {
            return new NormalDiscountPolicy();
        }

        if (planName.equalsIgnoreCase("prime")) {
            return new PrimeDiscountPolicy();
        } else if (planName.equalsIgnoreCase("platinum")) {
            return new PlatinumDiscountPolicy();
        } else if (planName.equalsIgnoreCase("normal")) {
            return new NormalDiscountPolicy();
        } else {
            throw new IllegalArgumentException("Unknown plan: " + planName);
        }
    }
}
