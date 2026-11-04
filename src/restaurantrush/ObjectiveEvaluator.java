package restaurantrush;

import java.util.OptionalDouble;

public class ObjectiveEvaluator {
    private static final int MIN_PAID = 4;
    private static final int MIN_REVENUE_CENTS = 4500;
    private static final int MIN_SATISFACTION = 60;

    public GameOutcome evaluate(Restaurant r) {
        OptionalDouble avg = r.averageSatisfaction();
        boolean enoughPaid = r.getPaidCount() >= MIN_PAID;
        boolean enoughRevenue = r.getRevenueCents() >= MIN_REVENUE_CENTS;
        boolean happyEnough = avg.isPresent() && avg.getAsDouble() >= MIN_SATISFACTION;
        if (enoughPaid && enoughRevenue && happyEnough) {
            return GameOutcome.VICTORY;
        }
        return GameOutcome.DEFEAT;
    }
}
