package restaurantrush;

public class TakeOrderAction implements StaffAction {
    private Customer customer;

    public TakeOrderAction(Customer customer) {
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
