package inventory;

public class StockUnderThreshold implements StockObserver {
    @Override
    public void onStockUpdate(Item item) {
        if (item.getStock() < item.getLowStockThreshold()) {
            System.out.println("Warning: Stock for item " + item.getName() + " is under threshold (" + item.getStock() + " left)");
        }
    }
}
