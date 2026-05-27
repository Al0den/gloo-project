package payment;

public class PaymentResult {
    private boolean success;
    private PaymentOutcome outcome;
    private String message;

    public PaymentResult(boolean success, PaymentOutcome outcome, String message) {
        this.success = success;
        this.outcome = outcome;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public PaymentOutcome getOutcome() {
        return outcome;
    }

    public String getMessage() {
        return message;
    }
}