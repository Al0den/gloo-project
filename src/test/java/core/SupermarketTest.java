package core;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import inventory.Category;
import inventory.Item;
import payment.BankCard;
import payment.Bill;
import users.Cart;
import users.Customer;

import org.junit.jupiter.api.Test;

class SupermarketTest {
    private static final double EPSILON = 0.0001;

    @Test
    void defaultManager() {
        Supermarket supermarket = new Supermarket();

        assertTrue(supermarket.userExists("ceo"));
        assertEquals("Manager", supermarket.getUser("ceo").getRole());
    }

    @Test
    void setupUsers() {
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
    void registerUsers() {
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
    void duplicateUser() {
        Supermarket supermarket = new Supermarket();

        assertThrows(
                IllegalArgumentException.class,
                () -> supermarket.registerManager("Other", "Boss", "ceo", "pwd")
        );
    }

    @Test
    void addItemData() {
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
    void addItemCategory() {
        Supermarket supermarket = new Supermarket();

        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        assertThrows(
                IllegalArgumentException.class,
                () -> supermarket.addItem("dairy", "milk", 1.30, 1.10, 20)
        );
    }

    @Test
    void restock() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        supermarket.restock("milk", 25);

        assertEquals(75, supermarket.getItem("milk").getStock());
    }

    @Test
    void badRestock() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("missing", 10)),
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("milk", 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.restock("milk", -1))
        );
    }

    @Test
    void stockThreshold() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        supermarket.setLowStockThreshold("milk", 20);

        assertEquals(20, supermarket.getItem("milk").getLowStockThreshold());
    }

    @Test
    void badStockThreshold() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 1.20, 1.00, 50);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.setLowStockThreshold("missing", 10)),
                () -> assertThrows(IllegalArgumentException.class, () -> supermarket.setLowStockThreshold("milk", -1))
        );
    }

    @Test
    void badCategoryDiscount() {
        Supermarket supermarket = new Supermarket();

        assertThrows(IllegalArgumentException.class, () -> supermarket.setCategoryDiscount("missing", 10.0));
    }

    @Test
    void revenue() {
        Supermarket supermarket = new Supermarket();

        supermarket.addRevenue(10.50);
        supermarket.addRevenue(4.25);

        assertEquals(14.75, supermarket.getRevenue(), EPSILON);
    }

    @Test
    void subscribePlan() {
        Supermarket supermarket = new Supermarket();
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "normal");
        Customer customer = (Customer) supermarket.getUser("carol");

        supermarket.subscribeToPlan(customer, "prime");

        assertEquals(50.0, supermarket.getRevenue(), EPSILON);
        assertEquals(80.0, customer.getDiscountPolicy().apply(100.0), EPSILON);
    }

    @Test
    void badDeliveryTime() {
        Supermarket supermarket = new Supermarket();
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "normal");
        Customer customer = (Customer) supermarket.getUser("carol");

        assertThrows(IllegalArgumentException.class, () -> supermarket.requestDelivery(customer, "1 Main St", "night"));

        assertFalse(customer.hasRequestedDelivery());
    }

    @Test
    void computeBill() {
        Supermarket supermarket = new Supermarket();
        supermarket.getCategoryOrCreate("dairy");
        supermarket.addItem("dairy", "milk", 10.00, 2.00, 10);
        supermarket.setCategoryDiscount("dairy", 10.0);
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "prime");

        Customer customer = (Customer) supermarket.getUser("carol");
        customer.requestDelivery("1 Main St", 5.0, supermarket.getDeliveryScheduler().getSlot("morning"));

        Cart cart = new Cart();
        cart.addItem(supermarket.getItem("milk"), supermarket.getItemCategory("milk"), 6);

        Bill bill = supermarket.computeBill(customer, cart);

        assertEquals(51.78, bill.getFinalAmount(), EPSILON);
    }

    @Test
    void saleOk() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 10.00, 2.00, 10);
        supermarket.setCategoryDiscount("dairy", 10.0);
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "prime");

        Customer customer = (Customer) supermarket.getUser("carol");
        customer.requestDelivery("1 Main St", 5.0, supermarket.getDeliveryScheduler().getSlot("morning"));

        Cart cart = new Cart();
        cart.addItem(supermarket.getItem("milk"), supermarket.getItemCategory("milk"), 6);

        BankCard card = new BankCard("1234", "0000", 100.0);
        supermarket.getTransactionSystem().registerCard(card);

        Bill bill = supermarket.finalizeSale(customer, cart, "1234", "0000");

        assertNotNull(bill);
        assertEquals(51.78, bill.getFinalAmount(), EPSILON);
        assertEquals(48.22, card.getBalance(), EPSILON);
        assertEquals(51.78, supermarket.getRevenue(), EPSILON);
        assertEquals(4, supermarket.getItem("milk").getStock());
        assertEquals(12.0, supermarket.getDeliveryScheduler().getSlot("morning").getBookedWeightKg(), EPSILON);
        assertFalse(customer.hasRequestedDelivery());
        assertEquals(0.0, cart.getTotalWeight(), EPSILON);
    }

    @Test
    void saleWrongPin() {
        Supermarket supermarket = new Supermarket();
        supermarket.addItem("dairy", "milk", 10.00, 2.00, 10);
        supermarket.registerCustomer("Carol", "Buyer", "carol", "1 Main St", "pwd", "normal");

        Customer customer = (Customer) supermarket.getUser("carol");
        customer.requestDelivery("1 Main St", 5.0, supermarket.getDeliveryScheduler().getSlot("morning"));

        Cart cart = new Cart();
        cart.addItem(supermarket.getItem("milk"), supermarket.getItemCategory("milk"), 2);

        BankCard card = new BankCard("1234", "0000", 100.0);
        supermarket.getTransactionSystem().registerCard(card);

        Bill bill = supermarket.finalizeSale(customer, cart, "1234", "9999");

        assertNull(bill);
        assertEquals(100.0, card.getBalance(), EPSILON);
        assertEquals(0.0, supermarket.getRevenue(), EPSILON);
        assertEquals(10, supermarket.getItem("milk").getStock());
        assertEquals(0.0, supermarket.getDeliveryScheduler().getSlot("morning").getBookedWeightKg(), EPSILON);
        assertTrue(customer.hasRequestedDelivery());
        assertEquals(4.0, cart.getTotalWeight(), EPSILON);
    }
}
