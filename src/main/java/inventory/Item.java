package inventory;

import java.util.List;
import java.util.ArrayList;

public class Item {
    private static final StockObserver lowStockObserver = new StockUnderThreshold();

    private String name;
    private double price;
    private double weight;
    private Integer stock;

    private int lowStockThreshold = 5;

    private List<StockObserver> stockObservers;

    public Item(String name, double price, double weight, Integer stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.stock = stock;
        this.stockObservers = new ArrayList<>();

        addStockObserver(lowStockObserver);
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public double getPrice() {
        return price; 
    }

    public double getWeight() {
        return weight;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;

        notifyStockObservers();
    }

    public void decreaseStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to decrease cannot be negative");
        }
        if (amount > stock) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        this.stock -= amount;

        notifyStockObservers();
    }

    private void notifyStockObservers() {
        for (StockObserver observer : stockObservers) {
            observer.onStockUpdate(this);
        }
    }

    public void addStockObserver(StockObserver observer) {
        stockObservers.add(observer);
    }

    public void removeStockObserver(StockObserver observer) {
        stockObservers.remove(observer);
    }

    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
}
