import java.util.Objects;

/**
 * Stage 1 pricing: applies only the customer's own pricing behaviour.
 * Promotion policies are introduced in Stage 3.
 */
public class PricingService {

    /**
     * Returns the price in cents when the customer orders the item.
     */
    public int quote(Customer c, MenuItem item, int turn) {
        Objects.requireNonNull(c, "customer must not be null");
        Objects.requireNonNull(item, "item must not be null");
        if (turn < 1) {
            throw new IllegalArgumentException("turn must be >= 1 but was " + turn);
        }
        return c.adjustBill(item.getPriceCents());
    }
}
