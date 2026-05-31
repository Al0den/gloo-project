package payment;

import java.util.Map;
import java.util.HashMap;

public class TransactionSystem {
    private Map<String, BankCard> bankCards = new HashMap<>();

    public void registerCard(BankCard card) {
        bankCards.put(card.getCardNumber(), card);
    } 
 
    public PaymentResult authorize(String cardNumber, String pin, double amount) {
        BankCard card = bankCards.get(cardNumber);

        if (card == null) {
            return new PaymentResult(false, PaymentOutcome.CARD_NOT_FOUND, "Card not found.");
        }

        if (!card.checkPin(pin)) {
            return new PaymentResult(false, PaymentOutcome.PIN_WRONG, "Wrong PIN.");
        }

        if (!card.hasEnoughBalance(amount)) {
            return new PaymentResult(false, PaymentOutcome.INSUFFICIENT_FUNDS, "Insufficient funds.");
        }

        card.debit(amount);
        return new PaymentResult(true, PaymentOutcome.SUCCESS, "Payment accepted.");
    }

    public PaymentResult forcePayment(String cardNumber, double amount) {
        BankCard card = bankCards.get(cardNumber);

        if (card == null) {
            return new PaymentResult(false, PaymentOutcome.CARD_NOT_FOUND, "Card not found.");
        }

        card.debit(amount);
        return new PaymentResult(true, PaymentOutcome.SUCCESS, "Payment accepted.");
    }

}
