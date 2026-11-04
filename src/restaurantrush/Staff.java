package restaurantrush;

import java.util.List;

public abstract class Staff {
    private String name;

    protected Staff(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<StaffAction> availableActions(Restaurant r, int turn);
}
