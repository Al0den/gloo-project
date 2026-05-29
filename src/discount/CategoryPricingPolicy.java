package discount;

import catalog.Item;

public interface CategoryPricingPolicy {
    public double apply(Item item, int quantity);
}
