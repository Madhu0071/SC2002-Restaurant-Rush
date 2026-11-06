package restaurantrush;

import java.util.List;

/**
 * A restaurant customer. Each customer type differs only in the behaviour it
 * overrides (waitLoss, adjustBill, chooseItem, onPaid), so the turn logic can
 * work with any Customer without checking its concrete type.
 *
 * Encapsulation: satisfaction is private and only changes through
 * loseSatisfaction() and recover(), which keep it within 0-100 via clamp().
 * The state only changes through markSeated(), placeOrder(), markServed(),
 * finishEating(), markPaid() and leave(); each rejects a call made from the
 * wrong state, so a customer cannot skip a step.
 */
public abstract class Customer {

    private final int id;
    private int satisfaction;
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

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

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

    /** Turn on which the food was served, or -1 if not served yet. */
    public int getServedOnTurn() {
        return servedOnTurn;
    }

    /** The customer's order, or null if no order has been placed. */
    public Order getOrder() {
        return order;
    }

    // ------------------------------------------------------------------
    // State transitions
    // ------------------------------------------------------------------

    /** WAITING_IN_QUEUE -> SEATED. */
    public void markSeated() {
        if (state != CustomerState.WAITING_IN_QUEUE) {
            throw new IllegalStateException("Customer " + id + " cannot be seated while " + state);
        }
        state = CustomerState.SEATED;
    }

    /** SEATED -> ORDERED. The order's price is already locked by the caller. */
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

    /** ORDERED -> EATING. Records the turn the food arrived. */
    public void markServed(int turn) {
        if (state != CustomerState.ORDERED) {
            throw new IllegalStateException("Customer " + id + " cannot be served while " + state);
        }
        servedOnTurn = turn;
        state = CustomerState.EATING;
    }

    /**
     * EATING -> READY_TO_PAY. A customer needs one full turn to eat, so this
     * is only allowed on a turn after the one they were served on.
     */
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

    /**
     * READY_TO_PAY -> PAID. This only changes the customer's state; the
     * caller marks the order PAID, calls onPaid(order) and records the payment.
     */
    public void markPaid() {
        if (state != CustomerState.READY_TO_PAY) {
            throw new IllegalStateException("Customer " + id + " cannot pay while " + state);
        }
        state = CustomerState.PAID;
    }

    /**
     * Customer leaves without paying (WAITING_IN_QUEUE, SEATED or ORDERED ->
     * LEFT_UNHAPPY). Any order they placed was not served yet, so it is
     * cancelled here. Freeing the table is the caller's job.
     */
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

    // ------------------------------------------------------------------
    // Satisfaction
    // ------------------------------------------------------------------

    /**
     * End-of-turn waiting penalty. Only a customer still waiting for a table
     * or food (WAITING_IN_QUEUE, SEATED or ORDERED) loses satisfaction; any
     * other customer loses nothing. The amount comes from waitLoss().
     */
    public void loseSatisfaction() {
        if (state != CustomerState.WAITING_IN_QUEUE
                && state != CustomerState.SEATED
                && state != CustomerState.ORDERED) {
            return;
        }
        satisfaction = clamp(satisfaction - waitLoss());
    }

    /** Raise satisfaction by the given points, up to 100. */
    public void recover(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Recovery points must not be negative: " + points);
        }
        satisfaction = clamp(satisfaction + points);
    }

    /**
     * Change satisfaction by a signed amount, kept within 0-100. Only for
     * subclasses that react to service after being served (when
     * loseSatisfaction() no longer applies).
     */
    protected void changeSatisfaction(int delta) {
        satisfaction = clamp(satisfaction + delta);
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    // ------------------------------------------------------------------
    // Polymorphic behaviour (overridden per customer type)
    // ------------------------------------------------------------------

    /** Satisfaction lost per waiting turn. Each customer type defines its own. */
    public abstract int waitLoss();

    /**
     * Customer-specific adjustment to a bill, in cents. The default is full
     * price; a customer type with a discount overrides this.
     */
    public int adjustBill(int cents) {
        return cents;
    }

    /**
     * Food choice. Customers pick their own food; the manager never chooses.
     *
     * Default rule (deterministic pseudo-random): take the menu's items in
     * order and pick index (id + turn) mod (number of items). The same
     * customer ordering on the same turn always gets the same item, so runs
     * are reproducible without a random seed. Because it only uses the menu's
     * list, any item later added to the menu can also be chosen.
     */
    public MenuItem chooseItem(Menu menu, int turn) {
        List<MenuItem> items = menu.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Menu has no items to choose from");
        }
        int index = Math.floorMod(id + turn, items.size());
        return items.get(index);
    }

    /** Hook called after this customer has paid. Does nothing by default. */
    public void onPaid(Order o) {
        // no special reaction by default
    }
}
