package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BankCardTest {
    private static final double EPSILON = 0.0001;

    @Test
    void checkPinAcceptsOnlyCorrectPin() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        assertTrue(card.checkPin("0000"));
        assertFalse(card.checkPin("1111"));
    }

    @Test
    void hasEnoughBalanceChecksAvailableMoney() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        assertTrue(card.hasEnoughBalance(100.0));
        assertFalse(card.hasEnoughBalance(100.01));
    }

    @Test
    void debitReducesBalance() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        card.debit(25.5);

        assertEquals(74.5, card.getBalance(), EPSILON);
    }
}
