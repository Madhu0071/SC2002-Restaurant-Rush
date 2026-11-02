package restaurantrush;

import java.util.List;

public class Waiter extends Staff {
    public Waiter() {
        super("");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        return null;
    }
}
