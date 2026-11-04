/**
 * A pricing promotion that PricingService applies to every quote
 * (see class diagram). Implemented by HappyHourPolicy.
 */
public interface PromotionPolicy {

    /**
     * Returns the bill in cents after this promotion has been applied.
     * Called by PricingService.quote() after the customer's own
     * adjustBill() (e.g. the VIP discount), so promotions always stack on
     * top of the customer discount.
     */
    int adjust(Customer c, int cents, int turn);

    /**
     * Called once at the start of every turn, after the manager has decided
     * whether to start any promotion for that turn.
     */
    void onTurnStart(int turn, Restaurant r);
}
