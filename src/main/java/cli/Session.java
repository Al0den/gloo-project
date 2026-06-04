package cli;

import users.User;
import users.Customer;
import users.Cashier;
import users.Manager;
import users.Cart;

public class Session {
    private User currentUser;
    private Customer checkoutCustomer;
    private Cart currentCart;

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void login(User user) {
        if (currentUser != null) {
            throw new IllegalStateException("A user is already logged in");
        }
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        this.checkoutCustomer = null;
        this.currentCart = null;
    }

    public boolean isManager() {
        return currentUser instanceof Manager;
    }

    public boolean isCashier() {
        return currentUser instanceof Cashier;
    }

    public boolean isCustomer() {
        return currentUser instanceof Customer;
    }

    public Customer getLoggedCustomer() {
        if (!(currentUser instanceof Customer)) {
            throw new IllegalStateException("Current user is not a customer");
        }
        return (Customer) currentUser;
    }

    public void startCheckout(Customer customer) {
        if (!isCashier()) {
            throw new IllegalStateException("Only a cashier can start checkout");
        }

        this.checkoutCustomer = customer;
        this.currentCart = new Cart();
    }

    public boolean hasActiveCheckout() {
        return currentCart != null && checkoutCustomer != null;
    }

    public Cart getCurrentCart() {
        if (currentCart == null) {
            throw new IllegalStateException("No active checkout");
        }
        return currentCart;
    }

    public Customer getCheckoutCustomer() {
        if (checkoutCustomer == null) {
            throw new IllegalStateException("No checkout customer");
        }
        return checkoutCustomer;
    }
 
    public void endCheckout() {
        this.checkoutCustomer = null;
        this.currentCart = null;
    }
}