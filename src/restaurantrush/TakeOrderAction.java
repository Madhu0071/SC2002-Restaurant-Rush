package restaurantrush;

public class TakeOrderAction implements StaffAction {
    private Customer customer;

    public TakeOrderAction(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String describe() {
        return "Take Customer " + customer.getId() + "'s order.";
    }

    @Override
    public ValidationResult validate(Restaurant r, int turn) {
        if (customer.getState() == CustomerState.WAITING_IN_QUEUE){
            return ValidationResult.fail("Customer " + customer.getId() + " has not been seated.");
        }

        if (customer.getState() == CustomerState.LEFT_UNHAPPY){
            return ValidationResult.fail("Customer " + customer.getId() + " has left unhappy.");
        }

        if (customer.getState() != CustomerState.SEATED){
            return ValidationResult.fail("Customer " + customer.getId() + " has already ordered.");
        }

        return ValidationResult.ok();
    }

    @Override
    public void execute(Restaurant r, int turn) {
       MenuItem item = customer.chooseItem(r.getMenu(), turn);
       int priceCents = r.getPricing().quote(customer, item, turn);

       Order order = new Order(customer, item, priceCents);
       customer.placeOrder(order);
    }
}
