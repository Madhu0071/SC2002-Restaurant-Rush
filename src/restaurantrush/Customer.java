package restaurantrush;

public abstract class Customer {
    private int id;
    private int arrivalTurn;
    private int satisfaction;
    private CustomerState state;
    private int servedOnTurn;
    private Order order;

    protected Customer(int id, int arrivalTurn) {
    }

    public int getId() {
        return 0;
    }

    public int getArrivalTurn() {
        return 0;
    }

    public int getSatisfaction() {
        return 0;
    }

    public CustomerState getState() {
        return null;
    }

    public int getServedOnTurn() {
        return 0;
    }

    public Order getOrder() {
        return null;
    }

    public void markSeated() {
    }

    public void placeOrder(Order o) {
    }

    public void markServed(int turn) {
    }

    public void finishEating(int turn) {
    }

    public void markPaid() {
    }

    public void leave() {
    }

    public void loseSatisfaction() {
    }

    public void recover(int points) {
    }

    private int clamp(int v) {
        return 0;
    }

    public abstract int waitLoss();

    public int adjustBill(int cents) {
        return 0;
    }

    public MenuItem chooseItem(Menu menu, int turn) {
        return null;
    }

    public void onPaid(Order o) {
    }
}
