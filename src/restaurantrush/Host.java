package restaurantrush;

import java.util.List;
import java.util.ArrayList;

public class Host extends Staff {
    public Host() {
        super("Host");
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
        return actionsList;
    }

}