package payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class PaymentResultTest {
    @Test
    void paymentResultStoresStatusOutcomeAndMessage() {
        PaymentResult result = new PaymentResult(false, PaymentOutcome.PIN_WRONG, "Wrong PIN.");

        assertFalse(result.isSuccess());
        assertEquals(PaymentOutcome.PIN_WRONG, result.getOutcome());
        assertEquals("Wrong PIN.", result.getMessage());
    }
}
