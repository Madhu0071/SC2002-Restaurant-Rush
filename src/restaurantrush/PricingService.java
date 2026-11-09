package restaurantrush;

import java.util.ArrayList;
import java.util.List;

public class PricingService {

    private final List<PromotionPolicy> policies = new ArrayList<>();

    public int quote(Customer c, MenuItem item, int turn) {
        if (c == null || item == null) {
            throw new IllegalArgumentException("Customer and item must not be null");
        }
        int cents = item.getPriceCents();
        cents = c.adjustBill(cents);
        for (PromotionPolicy p : policies) {
            cents = p.adjust(c, cents, turn);
        }
        return cents;
    }

    public void addPolicy(PromotionPolicy p) {
        if (p == null) {
            throw new IllegalArgumentException("Policy must not be null");
        }
        policies.add(p);
    }
}
