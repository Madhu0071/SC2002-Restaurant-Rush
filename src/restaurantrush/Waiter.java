package restaurantrush;

import java.util.List;
import java.util.ArrayList;

public class Waiter extends Staff {
    public Waiter() {
        super("Waiter");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        List<StaffAction> actionsList = new ArrayList<>();

        List<Customer> customerQueueList = r.getQueue();

        for(Customer customer : customerQueueList){
            Table table = r.freeTable();
            if (table != null){
                actionsList.add(new SeatCustomerAction(customer, table));
            }
        }

        for (Customer customer : r.getCustomers()){
            if (customer.getState() == CustomerState.SEATED){
                actionsList.add(new TakeOrderAction(customer));
            }
        }

        for (Customer customer : r.getCustomers()){
            Order order = customer.getOrder()
            if (order != null){
                if (order.isServable(turn)){
                    actionsList.add(new ServeOrderAction(order));
                }
            }
        }

        return actionsList;
    }
}
