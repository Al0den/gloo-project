package discount;

import catalog.Category;

public class DiscountPlan {
    private String name;

    private double globalDiscountPercentage = 0.0;
    private double globalDiscountMinimumCeiling = 0.0;

    private double oneTimeFee = 0.0;

    public DiscountPlan(String name, double globalDiscountPercentage, double globalDiscountMinimumCeiling, double oneTimeFee) {
        this.name = name;
        this.globalDiscountPercentage = globalDiscountPercentage;
        this.globalDiscountMinimumCeiling = globalDiscountMinimumCeiling;
        this.oneTimeFee = oneTimeFee;
    }

    public String getName() {
        return name;
    }

    public double applyGlobalDiscount(double originalPrice) {
        if (originalPrice < globalDiscountMinimumCeiling) {
            return originalPrice; // No discoutn if price < minimum ceiling
        }
        return originalPrice * (1 - globalDiscountPercentage / 100);
    }

    public double applyCategoryDiscount(Category category, double originalPrice) {
        return originalPrice * (1 - category.categoryDiscountPercentage / 100);
    }

    public double getOneTimeFee() {
        return oneTimeFee;
    }
}
