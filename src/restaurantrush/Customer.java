import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for every customer type (see class diagram).
 *
 * Polymorphic hooks called by GameController on a Customer reference:
 * waitLoss() (abstract), adjustBill(), chooseItem(), onPaid().
 *
 * Encapsulation: satisfaction is private and changes only through
 * loseSatisfaction() and recover(), which clamp it to 0..100 via clamp().
 * Both ignore customers who are no longer waiting (served, eating, ready to
 * pay, paid, left), as the assignment requires, so a customer's satisfaction
 * is frozen from the moment they are served.
 *
 * Allowed state changes in setState():
 *   WAITING_IN_QUEUE -> SEATED -> ORDERED -> EATING -> READY_TO_PAY -> PAID
 *   WAITING_IN_QUEUE / SEATED / ORDERED -> LEFT_UNHAPPY
 *
 * Members marked "not in diagram" are plain accessors for private fields
 * that would otherwise be unreachable.
 */
public abstract class Customer {

    private int id;
    private int satisfaction;
    private CustomerState state;
    private int arrivalTurn;
    private int servedOnTurn;
    private Order order;

    /** New customer: in the queue, satisfaction 100, not served, no order. */
    protected Customer(int id, int arrivalTurn) {
        if (arrivalTurn < 1) {
            throw new IllegalArgumentException("arrivalTurn must be >= 1 but was " + arrivalTurn);
        }
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING_IN_QUEUE;
        this.servedOnTurn = -1;
        this.order = null;
    }

    // ---- Operations from the class diagram ----

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    /**
     * @throws NullPointerException  if s is null
     * @throws IllegalStateException if the move is not an allowed state change
     */
    public void setState(CustomerState s) {
        Objects.requireNonNull(s, "state must not be null");
        boolean allowed;
        switch (state) {
            case WAITING_IN_QUEUE:
                allowed = (s == CustomerState.SEATED || s == CustomerState.LEFT_UNHAPPY);
                break;
            case SEATED:
                allowed = (s == CustomerState.ORDERED || s == CustomerState.LEFT_UNHAPPY);
                break;
            case ORDERED:
                allowed = (s == CustomerState.EATING || s == CustomerState.LEFT_UNHAPPY);
                break;
            case EATING:
                allowed = (s == CustomerState.READY_TO_PAY);
                break;
            case READY_TO_PAY:
                allowed = (s == CustomerState.PAID);
                break;
            default: // PAID and LEFT_UNHAPPY are terminal
                allowed = false;
                break;
        }
        if (!allowed) {
            throw new IllegalStateException(this + ": illegal state change " + state + " -> " + s);
        }
        this.state = s;
    }

    /** Applies this customer type's waiting penalty. No effect once the customer is served. */
    public void loseSatisfaction() {
        if (state != CustomerState.WAITING_IN_QUEUE
                && state != CustomerState.SEATED
                && state != CustomerState.ORDERED) {
            return;
        }
        satisfaction = clamp(satisfaction - waitLoss());
    }

    /**
     * Restores satisfaction (capped at 100). No effect once the customer is served.
     * @throws IllegalArgumentException if points is negative
     */
    public void recover(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("recover points must be >= 0 but was " + points);
        }
        if (state != CustomerState.WAITING_IN_QUEUE
                && state != CustomerState.SEATED
                && state != CustomerState.ORDERED) {
            return;
        }
        satisfaction = clamp(satisfaction + points);
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    /** Satisfaction points lost per waiting turn. */
    public abstract int waitLoss();

    /**
     * Final bill in cents; default is full price.
     * @throws IllegalArgumentException if cents is negative
     */
    public int adjustBill(int cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("bill must be >= 0 cents but was " + cents);
        }
        return cents;
    }

    /**
     * Default food rule (deterministic): the menu item at position
     * (customer id mod menu size). It does not depend on the turn, so the
     * manager cannot influence a customer's choice by delaying the order.
     * @throws NullPointerException  if menu is null
     * @throws IllegalStateException if the menu has no items
     */
    public MenuItem chooseItem(Menu menu, int turn) {
        Objects.requireNonNull(menu, "menu must not be null");
        List<MenuItem> items = menu.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Menu has no items to choose from");
        }
        return items.get(Math.floorMod(id, items.size()));
    }

    /**
     * Called once this customer's order has been paid.
     * @throws IllegalArgumentException if o is null or not this customer's order
     * @throws IllegalStateException    if the customer is not READY_TO_PAY
     */
    public void onPaid(Order o) {
        if (o == null || o != order) {
            throw new IllegalArgumentException(this + ": order does not belong to this customer");
        }
        setState(CustomerState.PAID);
    }

    // ---- Accessors for private fields (not in diagram) ----

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getServedOnTurn() {
        return servedOnTurn;
    }

    public void setServedOnTurn(int turn) {
        if (turn < 1) {
            throw new IllegalArgumentException("servedOnTurn must be >= 1 but was " + turn);
        }
        this.servedOnTurn = turn;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order o) {
        if (o == null) {
            throw new IllegalArgumentException(this + ": order must not be null");
        }
        this.order = o;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + id
                + " [" + state + ", satisfaction=" + satisfaction + "]";
    }
}
