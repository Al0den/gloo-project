package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class POSDeviceTest {
    private static final double EPSILON = 0.0001;

    @Test
    void processPayment() {
        TransactionSystem transactionSystem = new TransactionSystem();
        transactionSystem.registerCard(new BankCard("1234", "0000", 100.0));
        POSDevice posDevice = new POSDevice(transactionSystem);

        PaymentResult result = posDevice.processPayment("1234", "0000", 20.0);

        assertTrue(result.isSuccess());
        assertEquals(PaymentOutcome.SUCCESS, result.getOutcome());
    }

    @Test
    void simulateOnce() {
        TransactionSystem transactionSystem = new TransactionSystem();
        transactionSystem.registerCard(new BankCard("1234", "0000", 100.0));
        POSDevice posDevice = new POSDevice(transactionSystem);

        posDevice.simulateNextPayment(PaymentOutcome.INSUFFICIENT_FUNDS);

        PaymentResult forcedResult = posDevice.processPayment("1234", "0000", 20.0);
        PaymentResult normalResult = posDevice.processPayment("1234", "0000", 20.0);

        assertFalse(forcedResult.isSuccess());
        assertEquals(PaymentOutcome.INSUFFICIENT_FUNDS, forcedResult.getOutcome());
        assertTrue(normalResult.isSuccess());
        assertEquals(PaymentOutcome.SUCCESS, normalResult.getOutcome());
    }

    @Test
    void authDenied() {
        POSDevice posDevice = new POSDevice(new TransactionSystem());

        posDevice.simulateNextPayment(PaymentOutcome.AUTH_DENIED);
        PaymentResult result = posDevice.processPayment("1234", "0000", 20.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.AUTH_DENIED, result.getOutcome());
    }

    @Test
    void forcedFail() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 100.0);
        transactionSystem.registerCard(card);
        POSDevice posDevice = new POSDevice(transactionSystem);

        posDevice.simulateNextPayment(PaymentOutcome.PIN_WRONG);
        PaymentResult result = posDevice.processPayment("1234", "0000", 20.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.PIN_WRONG, result.getOutcome());
        assertEquals(100.0, card.getBalance(), EPSILON);
    }

    @Test
    void forcedSuccess() {
        TransactionSystem transactionSystem = new TransactionSystem();
        BankCard card = new BankCard("1234", "0000", 100.0);
        transactionSystem.registerCard(card);
        POSDevice posDevice = new POSDevice(transactionSystem);

        posDevice.simulateNextPayment(PaymentOutcome.SUCCESS);
        PaymentResult result = posDevice.processPayment("1234", "9999", 20.0);

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.PIN_WRONG, result.getOutcome());
        assertEquals(100.0, card.getBalance(), EPSILON);
    }
}
