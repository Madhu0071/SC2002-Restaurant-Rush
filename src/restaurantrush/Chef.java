package restaurantrush;

import java.util.List;

public class Chef extends Staff {
    public Chef() {
        super("");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        return null;
    }
}
