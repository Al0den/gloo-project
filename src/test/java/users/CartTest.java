package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import discount.NormalDiscountPolicy;
import discount.PrimeDiscountPolicy;
import inventory.Category;
import inventory.Item;
import inventory.PercentageCategoryPricingPolicy;
import payment.Bill;

import org.junit.jupiter.api.Test;

class CartTest {
    private static final double EPSILON = 0.0001;

    private Bill bill() {
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());
        return new Bill(customer);
    }

    @Test
    void addItemComputesTotalPriceAndWeight() {
        Cart cart = new Cart();
        Category dairy = new Category();
        Item milk = new Item("milk", 2.0, 1.5, 10);

        cart.addItem(milk, dairy, 3);

        assertEquals(6.0, cart.computeTotalAndFillBill(new NormalDiscountPolicy(), bill()), EPSILON);
        assertEquals(4.5, cart.getTotalWeight(), EPSILON);
    }

    @Test
    void addItemRejectsNonPositiveQuantity() {
        Cart cart = new Cart();
        Category dairy = new Category();
        Item milk = new Item("milk", 2.0, 1.5, 10);

        assertThrows(IllegalArgumentException.class, () -> cart.addItem(milk, dairy, 0));
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(milk, dairy, -1));
    }

    @Test
    void addingSameItemTwiceAccumulatesQuantity() {
        Cart cart = new Cart();
        Category dairy = new Category();
        Item yogurt = new Item("yogurt", 1.0, 0.2, 20);

        cart.addItem(yogurt, dairy, 2);
        cart.addItem(yogurt, dairy, 3);

        assertEquals(5.0, cart.computeTotalAndFillBill(new NormalDiscountPolicy(), bill()), EPSILON);
        assertEquals(1.0, cart.getTotalWeight(), EPSILON);
    }

    @Test
    void totalPriceUsesCategoryPolicyThenCustomerPlan() {
        Cart cart = new Cart();
        Category meat = new Category();
        meat.setPricingPolicy(new PercentageCategoryPricingPolicy(10.0));
        Item steak = new Item("steak", 30.0, 0.5, 10);

        cart.addItem(steak, meat, 2);

        assertEquals(43.2, cart.computeTotalAndFillBill(new PrimeDiscountPolicy(), bill()), EPSILON);
    }

    @Test
    void computeTotalAlsoStoresTotalInBill() {
        Cart cart = new Cart();
        Category dairy = new Category();
        Item milk = new Item("milk", 2.0, 1.5, 10);
        Bill bill = bill();

        cart.addItem(milk, dairy, 3);
        cart.computeTotalAndFillBill(new NormalDiscountPolicy(), bill);

        assertEquals(6.0, bill.getFinalAmount(), EPSILON);
    }

    @Test
    void finalizeSaleDecreasesStockAndEmptiesCart() {
        Cart cart = new Cart();
        Category dairy = new Category();
        Item cheese = new Item("cheese", 4.0, 0.3, 10);

        cart.addItem(cheese, dairy, 4);
        cart.finalizeSale();

        assertEquals(6, cheese.getStock());
        assertEquals(0.0, cart.computeTotalAndFillBill(new NormalDiscountPolicy(), bill()), EPSILON);
        assertEquals(0.0, cart.getTotalWeight(), EPSILON);
    }
}
