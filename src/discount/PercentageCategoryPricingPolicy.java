package discount;

import inventory.Item;

public class PercentageCategoryPricingPolicy implements CategoryPricingPolicy {
    private double discount = 0.0;

    @Override
    public double apply(Item item, int quantity) {
        double basePrice = item.getPrice() * quantity;
        return basePrice * (1 - discount/100);
    }

    public PercentageCategoryPricingPolicy(double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        this.discount = discount;
    }
}
