package restaurantrush;

public class SeatCustomerAction implements StaffAction {
    private Customer customer;
    private Table table;

    public SeatCustomerAction(Customer customer, Table table) {
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
