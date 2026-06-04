package payment;

public class POSDevice {
    private TransactionSystem transactionSystem;
    private PaymentOutcome forcedOutcome = null;

    public POSDevice(TransactionSystem transactionSystem) {
        this.transactionSystem = transactionSystem;
    }

    public void simulateNextPayment(PaymentOutcome outcome) {
        this.forcedOutcome = outcome;
    }

    public PaymentResult processPayment(String cardNumber, String pin, double amount) {
        if (forcedOutcome != null) {
            PaymentResult result;
            switch (forcedOutcome) {
                case SUCCESS:
                    result = transactionSystem.forcePayment(cardNumber, amount);
                    break;
                case CARD_NOT_FOUND:
                    result = new PaymentResult(false, PaymentOutcome.CARD_NOT_FOUND, "Card not found.");
                    break;
                case PIN_WRONG:
                    result = new PaymentResult(false, PaymentOutcome.PIN_WRONG, "Wrong PIN.");
                    break;
                case INSUFFICIENT_FUNDS:
                    result = new PaymentResult(false, PaymentOutcome.INSUFFICIENT_FUNDS, "Insufficient funds.");
                    break;
                default:
                    result = new PaymentResult(false, forcedOutcome, "Payment failed.");
            }
            forcedOutcome = null; // Reset after use
            return result;
        }

        return transactionSystem.authorize(cardNumber, pin, amount);
    }
}
