package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BankCardTest {
    private static final double EPSILON = 0.0001;

    @Test
    void badCard() {
        assertThrows(IllegalArgumentException.class, () -> new BankCard(null, "0000", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new BankCard("", "0000", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new BankCard("1234", null, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new BankCard("1234", "", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new BankCard("1234", "0000", -0.01));
    }

    @Test
    void zeroBalance() {
        BankCard card = new BankCard("1234", "0000", 0.0);

        assertEquals(0.0, card.getBalance(), EPSILON);
    }

    @Test
    void checkPin() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        assertTrue(card.checkPin("0000"));
        assertFalse(card.checkPin("1111"));
    }

    @Test
    void enoughBalance() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        assertTrue(card.hasEnoughBalance(100.0));
        assertFalse(card.hasEnoughBalance(100.01));
    }

    @Test
    void debit() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        card.debit(25.5);

        assertEquals(74.5, card.getBalance(), EPSILON);
    }

    @Test
    void badDebit() {
        BankCard card = new BankCard("1234", "0000", 100.0);

        assertThrows(IllegalArgumentException.class, () -> card.debit(0.0));
        assertThrows(IllegalArgumentException.class, () -> card.debit(-1.0));
        assertThrows(IllegalArgumentException.class, () -> card.debit(100.01));

        assertEquals(100.0, card.getBalance(), EPSILON);
    }
}
