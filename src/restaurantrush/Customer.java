package restaurantrush;

import java.util.List;

public abstract class Customer {

    private final int id;
    protected int satisfaction;
    private CustomerState state;
    private final int arrivalTurn;
    private int servedOnTurn;
    private Order order;

    protected Customer(int id, int arrivalTurn) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING_IN_QUEUE;
        this.servedOnTurn = -1;   // -1 = not served yet
        this.order = null;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getServedOnTurn() {
        return servedOnTurn;
    }

    public Order getOrder() {
        return order;
    }

    public void markSeated() {
        if (state != CustomerState.WAITING_IN_QUEUE) {
            throw new IllegalStateException("Customer " + id + " cannot be seated while " + state);
        }
        state = CustomerState.SEATED;
    }

    public void placeOrder(Order o) {
        if (o == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (state != CustomerState.SEATED) {
            throw new IllegalStateException("Customer " + id + " cannot place an order while " + state);
        }
        this.order = o;
        state = CustomerState.ORDERED;
    }

    public void markServed(int turn) {
        if (state != CustomerState.ORDERED) {
            throw new IllegalStateException("Customer " + id + " cannot be served while " + state);
        }
        servedOnTurn = turn;
        state = CustomerState.EATING;
    }

    public void finishEating(int turn) {
        if (state != CustomerState.EATING) {
            throw new IllegalStateException("Customer " + id + " cannot finish eating while " + state);
        }
        if (turn <= servedOnTurn) {
            throw new IllegalStateException("Customer " + id + " was served on turn "
                    + servedOnTurn + " and cannot finish eating until a later turn");
        }
        state = CustomerState.READY_TO_PAY;
    }

    public void markPaid() {
        if (state != CustomerState.READY_TO_PAY) {
            throw new IllegalStateException("Customer " + id + " cannot pay while " + state);
        }
        state = CustomerState.PAID;
    }

    public void leave() {
        if (state != CustomerState.WAITING_IN_QUEUE
                && state != CustomerState.SEATED
                && state != CustomerState.ORDERED) {
            throw new IllegalStateException("Customer " + id + " cannot leave while " + state);
        }
        if (order != null) {
            order.cancel();
        }
        state = CustomerState.LEFT_UNHAPPY;
    }

    public void loseSatisfaction() {
        if (state != CustomerState.WAITING_IN_QUEUE
                && state != CustomerState.SEATED
                && state != CustomerState.ORDERED) {
            return;
        }
        satisfaction = clamp(satisfaction - waitLoss());
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }
    
    public abstract int waitLoss();

    public int adjustBill(int cents) {
        return cents;
    }

    public MenuItem chooseItem(Menu menu, int turn) {
        List<MenuItem> items = menu.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Menu has no items to choose from");
        }
        int index = Math.floorMod(id + turn, items.size());
        return items.get(index);
    }

    public void onPaid(Order o) {
    }
}
