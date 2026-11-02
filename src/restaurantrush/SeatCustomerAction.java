package restaurantrush;

public class SeatCustomerAction implements StaffAction {
    private Customer customer;
    private Table table;

    public SeatCustomerAction(Customer customer, Table table) {
        this.customer = customer;
        this.table = table;
    }

    @Override
    public String describe() {
        return "Seat Customer " + customer.getId();
    }

    @Override
    public ValidationResult validate(Restaurant r, int turn) {
        if (customer.getState() != CustomerState.WAITING_IN_QUEUE){
            return ValidationResult.fail("Customer is no longer waiting.");
        }

        if (!table.isFree()){
            return ValidationResult.fail("Table is no longer free.");
        }

        return ValidationResult.ok();
    }

    @Override
    public void execute(Restaurant r, int turn) {
        return;
    }
}
