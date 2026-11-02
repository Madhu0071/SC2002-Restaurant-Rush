package restaurantrush;

public class ServeOrderAction implements StaffAction {
    private Order order;

    public ServeOrderAction(Order order) {
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public ValidationResult validate(Restaurant r, int turn) {
        return null;
    }

    @Override
    public void execute(Restaurant r, int turn) {
    }
}
