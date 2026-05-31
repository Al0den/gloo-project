package core;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import inventory.Category;
import inventory.Item;
import users.Cart;
import users.Customer;

import org.junit.jupiter.api.Test;

class SupermarketTest {
    private static final double EPSILON = 0.0001;

    @Test
    void constructorRegistersDefaultManager() {
        Supermarket supermarket = new Supermarket();

        assertTrue(supermarket.userExists("ceo"));
        assertEquals("Manager", supermarket.getUser("ceo").getRole());
    }

    @Test
    void setupRegistersDefaultCashierAndCustomer() {
        Supermarket supermarket = new Supermarket();

        supermarket.setup();
        supermarket.setup();

        assertAll(
                () -> assertTrue(supermarket.userExists("cashier")),
                () -> assertEquals("Cashier", supermarket.getUser("cashier").getRole()),
                () -> assertTrue(supermarket.userExists("customer")),
                () -> assertEquals("Customer", supermarket.getUser("customer").getRole())
        );
    }

    @Test
    void registerUsersStoresUsersWithExpectedRoles() {
        Supermarket supermarket = new Supermarket();

        supermarket.registerManager("Alice", "Boss", "alice", "pwd");
        supermarket.registerCashier("Bob", "Till", "bob", "pwd");
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "normal");

        assertAll(
                () -> assertEquals("Manager", supermarket.getUser("alice").getRole()),
                () -> assertEquals("Cashier", supermarket.getUser("bob").getRole()),
                () -> assertEquals("Customer", supermarket.getUser("carol").getRole())
        );
    }

    @Test
    void registerUserRejectsDuplicateUsername() {
        Supermarket supermarket = new Supermarket();

        assertThrows(
                IllegalArgumentException.class,
                () -> supermarket.registerManager("Other", "Boss", "ceo", "pwd")
        );
    }

    @Test
    void addItemStoresPriceWeightStockAndCategory() {
        Supermarket supermarket = new Supermarket();
        Category category = supermarket.getCategoryOrCreate("dairy");

        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        Item milk = supermarket.getItem("milk");
        assertNotNull(milk);
        assertAll(
                () -> assertEquals(1.20, milk.getPrice(), EPSILON),
                () -> assertEquals(1.00, milk.getWeight(), EPSILON),
                () -> assertEquals(50, milk.getStock()),
                () -> assertSame(category, supermarket.getItemCategory("milk"))
        );
    }

    @Test
    void addItemCreatesMissingCategoryAndRejectsDuplicateItem() {
        Supermarket supermarket = new Supermarket();

        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        assertThrows(
                IllegalArgumentException.class,
                () -> supermarket.addItem("dairy", "milk", 1.30, 1.10, 20)
        );
    }

    @Test
    void restockIncreasesExistingItemStock() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        supermarket.restock("milk", 25);

        assertEquals(75, supermarket.getItem("milk").getStock());
    }

    @Test
    void restockRejectsUnknownItemAndNonPositiveQuantity() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("missing", 10)),
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("milk", 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("milk", -1))
        );
    }

    @Test
    void addRevenueAccumulatesRevenue() {
        Supermarket supermarket = new Supermarket();

        supermarket.addRevenue(10.50);
        supermarket.addRevenue(4.25);

        assertEquals(14.75, supermarket.getRevenue(), EPSILON);
    }

    @Test
    void computeBillAppliesCategoryDiscountCustomerPlanAndDeliveryFee() {
        Supermarket supermarket = new Supermarket();
        supermarket.getCategoryOrCreate("dairy");
        supermarket.addItem("dairy", "milk", 10.00, 2.00, 10);
        supermarket.setCategoryDiscount("dairy", 10.0);
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "prime");

        Customer customer = (Customer) supermarket.getUser("carol");
        customer.requestDelivery("1 Main St", 5.0);

        Cart cart = new Cart();
        cart.addItem(supermarket.getItem("milk"), supermarket.getItemCategory("milk"), 6);

        double total = supermarket.computeBill(customer, cart);

        assertEquals(51.78, total, EPSILON);
    }
}
