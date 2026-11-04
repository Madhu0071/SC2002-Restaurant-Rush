import java.util.Objects;

/**
 * Happy Hour: a pricing promotion the manager may switch on ONCE per game.
 *
 * Rules (assignment section 4.3):
 *  - Activated at the start of a turn; lasts that turn and the next.
 *  - Orders taken while active get 20% off the bill (applied after the
 *    VIP discount, because PricingService asks the customer first).
 *  - At the start of each active turn, customers still waiting for a table
 *    or food recover 15 satisfaction (max 100; Customer.recover() clamps it
 *    and ignores customers who are no longer waiting).
 *
 * Expected controller order each turn:
 *     manager decides  ->  activate(turn)  ->  onTurnStart(turn, restaurant)
 *     ->  turn steps (quote() is called when an order is taken).
 * After onTurnStart on the activation turn getTurnsRemaining() is 2, on the
 * next turn 1, then 0.
 *
 * Whether the policy is active is derived from the turn number, not from a
 * counter that has to be decremented, so a discount can never last longer
 * than two turns and the two counts can never disagree.
 */
public class HappyHourPolicy implements PromotionPolicy {

    private static final int DISCOUNT = 20;   // percent off
    private static final int RECOVERY = 15;   // satisfaction points per active turn
    private static final int DURATION = 2;    // this turn and the next

    private boolean used = false;
    private int turnsRemaining = 0;

    // Private bookkeeping (no change to the public interface in the diagram).
    private int activatedOnTurn = 0;
    private int lastRecoveryTurn = 0;

    /**
     * Switches Happy Hour on, starting with {@code turn}.
     * @throws IllegalArgumentException if turn is less than 1
     * @throws IllegalStateException    if Happy Hour was already used this game
     */
    public void activate(int turn) {
        if (turn < 1) {
            throw new IllegalArgumentException("turn must be >= 1 but was " + turn);
        }
        if (used) {
            throw new IllegalStateException("Happy Hour has already been used this game");
        }
        used = true;
        activatedOnTurn = turn;
        turnsRemaining = DURATION;
    }

    /** Active turns left, including the current one (for the start-of-turn display). */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /**
     * 20% off while active, rounded half-up to the nearest cent; unchanged otherwise.
     * @throws NullPointerException     if c is null
     * @throws IllegalArgumentException if cents is negative or turn is less than 1
     */
    @Override
    public int adjust(Customer c, int cents, int turn) {
        Objects.requireNonNull(c, "customer must not be null");
        if (cents < 0) {
            throw new IllegalArgumentException("bill must be >= 0 cents but was " + cents);
        }
        if (turn < 1) {
            throw new IllegalArgumentException("turn must be >= 1 but was " + turn);
        }
        if (!isActiveOn(turn)) {
            return cents;
        }
        return (int) (((long) cents * (100 - DISCOUNT) + 50) / 100);
    }

    /**
     * Updates the remaining duration and, on active turns, restores
     * satisfaction of everyone waiting in the queue or seated at a table.
     * Calling it twice in the same turn does not recover twice.
     * @throws NullPointerException     if r is null
     * @throws IllegalArgumentException if turn is less than 1
     */
    @Override
    public void onTurnStart(int turn, Restaurant r) {
        Objects.requireNonNull(r, "restaurant must not be null");
        if (turn < 1) {
            throw new IllegalArgumentException("turn must be >= 1 but was " + turn);
        }
        if (!used) {
            return;
        }
        if (isActiveOn(turn)) {
            turnsRemaining = activatedOnTurn + DURATION - turn;
        } else {
            if (turn >= activatedOnTurn + DURATION) {
                turnsRemaining = 0;
            }
            return;
        }
        if (turn == lastRecoveryTurn) {
            return; // already recovered this turn
        }
        lastRecoveryTurn = turn;
        for (Customer c : r.getQueue()) {
            c.recover(RECOVERY);
        }
        for (Table t : r.getTables()) {
            Customer occupant = t.getOccupant();
            if (occupant != null) {
                occupant.recover(RECOVERY);
            }
        }
    }

    /** True if Happy Hour has been activated and {@code turn} is one of its turns. */
    private boolean isActiveOn(int turn) {
        return used && turn >= activatedOnTurn && turn < activatedOnTurn + DURATION;
    }
}
