package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CashierTest {
    @Test
    void cashierHasCashierRoleAndCanLoginWithPassword() {
        Cashier cashier = new Cashier("cashier1", "Bob", "Durand", "1234");

        assertEquals("Cashier", cashier.getRole());
        assertTrue(cashier.checkPassword("1234"));
    }
}
