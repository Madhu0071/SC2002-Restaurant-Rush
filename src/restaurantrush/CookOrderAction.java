package restaurantrush;

public class CookOrderAction implements StaffAction {
    private Order order;

    public CookOrderAction(Order order) {
        this.order = order;
    }

    @Override
    public String describe() {
        return "Cook Customer " + order.getCustomer().getId() + "'s order.";
    }

    @Override
    public ValidationResult validate(Restaurant r, int turn) {
        if (order.getStatus() == OrderStatus.CANCELLED){
            return ValidationResult.fail("Order has already been cancelled.");
        }

        if (order.getStatus() == OrderStatus.READY){
            return ValidationResult.fail("Order has already been cooked.");
        }

        if (order.getStatus() == OrderStatus.SERVED){
            return ValidationResult.fail("Order has already been served.");
        }

        if (order.getStatus() == OrderStatus.PAID){
            return ValidationResult.fail("Order has already been paid for.");
        }

        return ValidationResult.ok();
    }

    @Override
    public void execute(Restaurant r, int turn) {
        order.advance(turn);
    }
}
