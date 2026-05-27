package payment;

public class BankCard {
    private String cardNumber;
    private String pin;
    private double balance;

    public BankCard(String cardNumber, String pin, double balance) {
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
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}