package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import discount.NormalDiscountPolicy;
import users.Customer;

import org.junit.jupiter.api.Test;

class BillTest {
    private static final double EPSILON = 0.0001;

    private Bill bill() {
        Customer customer = new Customer("customer", "Carol", "Buyer", "1 Main St", "pwd", new NormalDiscountPolicy());
        return new Bill(customer);
    }

    @Test
    void finalAmount() {
        Bill bill = bill();

        bill.setTotalAmount(43.20);
        bill.setDeliveryFee(8.58);

        assertEquals(51.78, bill.getFinalAmount(), EPSILON);
    }

    @Test
    void badBillAmount() {
        Bill bill = bill();

        assertThrows(IllegalArgumentException.class, () -> bill.setTotalAmount(-0.01));
        assertThrows(IllegalArgumentException.class, () -> bill.setDeliveryFee(-0.01));
    }
}
