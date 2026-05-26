package discount;

import catalog.Category;

public class DiscountPlan {
    private String name;
    private double globalDiscountPercentage;

    public DiscountPlan(String name, double globalDiscountPercentage) {
        this.name = name;
        this.globalDiscountPercentage = globalDiscountPercentage;
    }

    public String getName() {
        return name;
    }

    public double applyGlobalDiscount(double originalPrice) {
        return originalPrice * (1 - globalDiscountPercentage / 100);
    }

    public double applyCategoryDiscount(Category category, double originalPrice) {
        return originalPrice * (1 - category.categoryDiscountPercentage / 100);
    }
}
