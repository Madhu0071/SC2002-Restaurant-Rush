import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Prices an item for a customer. Only prices: it holds no game state.
 *
 * quote() = menu price -> the customer's own adjustment (VIP 10% off, via
 * Customer.adjustBill(), no type checks) -> each registered promotion in
 * registration order (e.g. Happy Hour 20% off). The result is the price to
 * store in the Order when it is taken, so later changes never touch it.
 *
 * The policy list is empty in Stage 1; Stage 3 only registers a policy with
 * addPolicy(), so this class never needs rewriting.
 */
public class PricingService {

    private List<PromotionPolicy> policies = new ArrayList<>();

    /**
     * Price in cents the customer pays for {@code item} when ordering on {@code turn}.
     * @throws NullPointerException     if c or item is null
     * @throws IllegalArgumentException if turn is less than 1
     */
    public int quote(Customer c, MenuItem item, int turn) {
        Objects.requireNonNull(c, "customer must not be null");
        Objects.requireNonNull(item, "item must not be null");
        if (turn < 1) {
            throw new IllegalArgumentException("turn must be >= 1 but was " + turn);
        }
        int bill = c.adjustBill(item.getPriceCents());
        for (PromotionPolicy p : policies) {
            bill = p.adjust(c, bill, turn);
        }
        return bill;
    }

    /**
     * Registers a promotion.
     * @throws NullPointerException     if p is null
     * @throws IllegalArgumentException if p is already registered (it would apply twice)
     */
    public void addPolicy(PromotionPolicy p) {
        Objects.requireNonNull(p, "policy must not be null");
        if (policies.contains(p)) {
            throw new IllegalArgumentException("policy is already registered");
        }
        policies.add(p);
    }
}
