package restaurantrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cashier extends Staff {
    public Cashier() {
        super("Cashier");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        List<StaffAction> actionList = new ArrayList<>();
        return actionList;
    }

    public Optional<Customer> collectPayment(Restaurant r, int turn) {
        List<Customer> customerList = r.getCustomers();

        Customer oldestCustomer = null;

        for (Customer customer : customerList){
            if (customer.getState() == CustomerState.READY_TO_PAY && turn >= customer.getServedOnTurn() + 2){
                if (oldestCustomer == null){
                    oldestCustomer = customer;
                }

                else if (customer.getServedOnTurn() < oldestCustomer.getServedOnTurn()){
                    oldestCustomer = customer;
                }
            }
        }

        if (oldestCustomer != null){
            int priceCents = oldestCustomer.getOrder().getPriceCents();
            r.recordPayment(oldestCustomer, priceCents);
            oldestCustomer.markPaid();
            oldestCustomer.getOrder().markPaid();
            oldestCustomer.onPaid(oldestCustomer.getOrder());
            Table table = r.tableOf(oldestCustomer);
            table.release();
            return Optional.of(oldestCustomer);
        }

        return Optional.empty();
    }
}
