package restaurantrush;

import java.util.List;
import java.util.Optional;

public class Cashier extends Staff {
    public Cashier() {
        super("");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        return null;
    }

    public Optional<Customer> collectPayment(Restaurant r, int turn) {
        return null;
    }
}
