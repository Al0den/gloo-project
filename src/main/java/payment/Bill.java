package payment;

import inventory.Item;
import inventory.Category;
import users.Customer;

import java.util.List;
import java.util.ArrayList;



public class Bill {
    public class ItemFinalized {
        Category category;
        Item item;
        int quantity;
        double finalPrice;   
    }

    private List<ItemFinalized> finalizedItems;

    private double totalAmount = 0.0;
    private double deliveryFee = 0.0;

    private Customer customer;

    private boolean status = false; // false = unpaid, true = paid

    public Bill(Customer customer) {
        this.customer = customer;

        this.finalizedItems = new ArrayList<>();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void onSale(Category category, Item item, int quantity, double finalPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (finalPrice < 0) {
            throw new IllegalArgumentException("Final price cannot be negative");
        }

        ItemFinalized itemFinalized = new ItemFinalized();
        itemFinalized.category = category;
        itemFinalized.item = item;
        itemFinalized.quantity = quantity;
        itemFinalized.finalPrice = finalPrice;

        finalizedItems.add(itemFinalized);
    }

    public double getFinalAmount() {
        return totalAmount + deliveryFee;
    }
    
    public void setDeliveryFee(double deliveryFee) {
        if (deliveryFee < 0) {
            throw new IllegalArgumentException("Delivery fee cannot be negative");
        }
        this.deliveryFee = deliveryFee;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        String result = "Bill for customer: " + customer.getUsername() + "\n\n";
        result += "Items:\n";
        for (ItemFinalized itemFinalized : finalizedItems) {
            result += "- " + itemFinalized.item.getName() + " x " + itemFinalized.quantity + " @ " + Math.round(itemFinalized.finalPrice/itemFinalized.quantity * 100.0) / 100.0 + " each\n";
        }
        result += "\n";
        result += "Subtotal: " + Math.round(totalAmount * 100.0) / 100.0 + "\n";
        result += "Delivery fee: " + Math.round(deliveryFee * 100.0) / 100.0 + "\n";
        result += "Total: " + Math.round(getFinalAmount() * 100.0) / 100.0 + "\n";

        String paymentStatus = status ? "PAID" : "UNPAID";
        result += "\nStatus: " + paymentStatus + "\n";
        return result;
    }
}
