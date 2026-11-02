package restaurantrush;

public class Order {
    private Customer customer;
    private MenuItem item;
    private int priceCents;
    private int unitsDone;
    private int readyOnTurn;
    private OrderStatus status;

    public Order(Customer customer, MenuItem item, int priceCents) {
    }

    public Customer getCustomer() {
        return null;
    }

    public MenuItem getItem() {
        return null;
    }

    public int getPriceCents() {
        return 0;
    }

    public OrderStatus getStatus() {
        return null;
    }

    public void advance(int turn) {
    }

    public boolean isServable(int turn) {
        return false;
    }

    public void markServed() {
    }

    public void markPaid() {
    }

    public void cancel() {
    }
}
