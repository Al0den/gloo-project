package discount;

import inventory.Item;

public interface CategoryPricingPolicy {
    public double apply(Item item, int quantity);
}
