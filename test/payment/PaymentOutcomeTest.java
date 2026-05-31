package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentOutcomeTest {
    @Test
    void enumContainsExpectedPaymentOutcomes() {
        assertEquals(PaymentOutcome.SUCCESS, PaymentOutcome.valueOf("SUCCESS"));
        assertEquals(PaymentOutcome.INSUFFICIENT_FUNDS, PaymentOutcome.valueOf("INSUFFICIENT_FUNDS"));
        assertEquals(PaymentOutcome.PIN_WRONG, PaymentOutcome.valueOf("PIN_WRONG"));
        assertEquals(PaymentOutcome.AUTH_DENIED, PaymentOutcome.valueOf("AUTH_DENIED"));
        assertEquals(PaymentOutcome.CARD_NOT_FOUND, PaymentOutcome.valueOf("CARD_NOT_FOUND"));
    }
}
