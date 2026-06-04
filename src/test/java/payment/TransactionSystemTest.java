package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TransactionSystemTest {
    private static final double EPSILON = 0.0001;

    @Test
    void authOk() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 100.0);
        transactionSystem.registerCard(card);

        PaymentResult result = transactionSystem.authorize("1234", "0000", 40.0);

        assertTrue(result.isSuccess());
        assertEquals(PaymentOutcome.SUCCESS, result.getOutcome());
        assertEquals(60.0, card.getBalance(), EPSILON);
    }

    @Test
    void unknownCard() {
        TransactionSystem transactionSystem = new TransactionSystem();

        PaymentResult result = transactionSystem.authorize("missing", "0000", 10.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.CARD_NOT_FOUND, result.getOutcome());
    }

    @Test
    void wrongPin() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 100.0);
        transactionSystem.registerCard(card);

        PaymentResult result = transactionSystem.authorize("1234", "9999", 40.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.PIN_WRONG, result.getOutcome());
        assertEquals(100.0, card.getBalance(), EPSILON);
    }

    @Test
    void noMoney() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 10.0);
        transactionSystem.registerCard(card);

        PaymentResult result = transactionSystem.authorize("1234", "0000", 40.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.INSUFFICIENT_FUNDS, result.getOutcome());
        assertEquals(10.0, card.getBalance(), EPSILON);
    }

    @Test
    void forceOk() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 100.0);
        transactionSystem.registerCard(card);

        PaymentResult result = transactionSystem.forcePayment("1234", 25.5);

        assertTrue(result.isSuccess());
        assertEquals(PaymentOutcome.SUCCESS, result.getOutcome());
        assertEquals(74.5, card.getBalance(), EPSILON);
    }

    @Test
    void forceUnknown() {
        TransactionSystem transactionSystem = new TransactionSystem();

        PaymentResult result = transactionSystem.forcePayment("missing", 10.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.CARD_NOT_FOUND, result.getOutcome());
    }
}
