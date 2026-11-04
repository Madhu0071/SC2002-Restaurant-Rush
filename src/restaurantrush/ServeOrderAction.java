package restaurantrush;

public class ServeOrderAction implements StaffAction {
    private Order order;

    public ServeOrderAction(Order order) {
        this.order = order;
    }

    @Override
    public String describe() {
        return "Serve Customer " + order.getCustomer().getId() + "'s order.";
    }

    @Override
    public ValidationResult validate(Restaurant r, int turn) {
        if (order.getStatus() == OrderStatus.PLACED || order.getStatus() == OrderStatus.COOKING){
            return ValidationResult.fail("Order is not yet ready to be served.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED){
            return ValidationResult.fail("Order has already been cancelled.");
        }

        if (order.getStatus() == OrderStatus.PAID){
            return ValidationResult.fail("Order has already been paid for.");
        }
        if (order.getStatus() == OrderStatus.SERVED){
            return ValidationResult.fail("Order has already been served.");
        }

        if (!order.isServable(turn)){
            return ValidationResult.fail("Order cannot be served yet.");
        }

        return ValidationResult.ok();
    }

    @Override
    public void execute(Restaurant r, int turn) {
        order.markServed();
        order.getCustomer().markServed(turn);
    }
}
