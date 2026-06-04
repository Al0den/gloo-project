package payment;

public class BankCard {
    private String cardNumber;
    private String pin;
    private double balance;

    public BankCard(String cardNumber, String pin, double balance) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number cannot be empty");
        }
        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException("PIN cannot be empty");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

    public boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    public void debit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (!hasEnoughBalance(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}