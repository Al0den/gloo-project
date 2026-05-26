package catalog;

public class Item {
    private String name;
    private double price;
    private double weight;

    private Integer stock;

    public Item(String name, double price, double weight, Integer stock) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
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
}
