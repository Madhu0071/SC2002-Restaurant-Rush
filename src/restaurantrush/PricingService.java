package restaurantrush;

/**
 * Prices an order at the moment it is taken. Its only job is pricing (SRP).
 *
 * The price starts at the menu item's price and is then adjusted by the
 * customer (Customer.adjustBill). The returned amount is in integer cents and
 * is locked into the Order by the caller, so later changes do not affect it.
 */
public class PricingService {

    /**
     * @param c    the customer placing the order
     * @param item the item they chose
     * @param turn the turn the order is taken on (part of the pricing contract
     *             so that turn-dependent pricing can be added later)
     * @return the bill in cents
     */
    public int quote(Customer c, MenuItem item, int turn) {
        if (c == null || item == null) {
            throw new IllegalArgumentException("Customer and item must not be null");
        }
        int cents = item.getPriceCents();
        return c.adjustBill(cents);
    }
}
