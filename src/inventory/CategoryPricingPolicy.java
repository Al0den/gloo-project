package inventory;

public interface CategoryPricingPolicy {
    public double apply(Item item, int quantity);
}
