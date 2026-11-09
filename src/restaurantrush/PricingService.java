package restaurantrush;

public class PricingService {

    public int quote(Customer c, MenuItem item, int turn) {
        if (c == null || item == null) {
            throw new IllegalArgumentException("Customer and item must not be null");
        }
        return item.getPriceCents();
        
    }
}
