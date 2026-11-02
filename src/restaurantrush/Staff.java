package restaurantrush;

import java.util.List;

public abstract class Staff {
    private String name;

    protected Staff(String name) {
    }

    public String getName() {
        return null;
    }

    public abstract List<StaffAction> availableActions(Restaurant r, int turn);
}
