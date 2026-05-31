package inventory;

public class NormalCategoryPricingPolicy implements CategoryPricingPolicy {
    @Override
    public double apply(Item item, int quantity) {
        return item.getPrice() * quantity;
    }
    
}
