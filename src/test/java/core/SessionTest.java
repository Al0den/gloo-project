package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import discount.NormalDiscountPolicy;
import users.Cashier;
import users.Customer;
import users.Manager;

import org.junit.jupiter.api.Test;

import cli.Session;

class SessionTest {
    @Test
    void loginAndLogoutUpdateCurrentUser() {
        Session session = new Session();
        Manager manager = new Manager("manager", "Alice", "Boss", "pwd");

        session.login(manager);

        assertTrue(session.isLoggedIn());
        assertTrue(session.isManager());
        assertSame(manager, session.getCurrentUser());

        session.logout();

        assertFalse(session.isLoggedIn());
    }

    @Test
    void loginRejectsSecondUserWhileAlreadyLoggedIn() {
        Session session = new Session();
        session.login(new Manager("manager", "Alice", "Boss", "pwd"));

        assertThrows(
                IllegalStateException.class,
                () -> session.login(new Cashier("cashier", "Bob", "Till", "pwd"))
        );
    }

    @Test
    void roleHelpersIdentifyCurrentUserType() {
        Session session = new Session();
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());

        session.login(customer);

        assertTrue(session.isCustomer());
        assertFalse(session.isCashier());
        assertSame(customer, session.getLoggedCustomer());
    }

    @Test
    void getLoggedCustomerRejectsNonCustomer() {
        Session session = new Session();
        session.login(new Cashier("cashier", "Bob", "Till", "pwd"));

        assertThrows(IllegalStateException.class, session::getLoggedCustomer);
    }

    @Test
    void startCheckoutRequiresCashierAndCreatesCart() {
        Session session = new Session();
        Cashier cashier = new Cashier("cashier", "Bob", "Till", "pwd");
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());

        session.login(cashier);
        session.startCheckout(customer);

        assertTrue(session.hasActiveCheckout());
        assertSame(customer, session.getCheckoutCustomer());
        assertSame(session.getCurrentCart(), session.getCurrentCart());
    }

    @Test
    void startCheckoutRejectsNonCashier() {
        Session session = new Session();
        session.login(new Manager("manager", "Alice", "Boss", "pwd"));
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());

        assertThrows(IllegalStateException.class, () -> session.startCheckout(customer));
    }

    @Test
    void currentCartRejectsWhenNoCheckoutIsActive() {
        Session session = new Session();

        assertFalse(session.hasActiveCheckout());
        assertThrows(IllegalStateException.class, session::getCurrentCart);
        assertThrows(IllegalStateException.class, session::getCheckoutCustomer);
    }

    @Test
    void endCheckoutClearsCheckout() {
        Session session = new Session();
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());
        session.login(new Cashier("cashier", "Bob", "Till", "pwd"));
        session.startCheckout(customer);

        session.endCheckout();

        assertFalse(session.hasActiveCheckout());
        assertThrows(IllegalStateException.class, session::getCurrentCart);
        assertThrows(IllegalStateException.class, session::getCheckoutCustomer);
    }
}
