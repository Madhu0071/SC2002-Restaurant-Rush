package restaurantrush;

import java.util.List;
import java.util.ArrayList;

public class Chef extends Staff {
    public Chef() {
        super("Chef");
    }

    @Override
    public List<StaffAction> availableActions(Restaurant r, int turn) {
        List<StaffAction> actionList = new ArrayList<>();

        List<Customer> customerList = r.getCustomers();
        for (Customer customer : customerList){
            Order order = customer.getOrder();
            if (order != null){
                if (order.getStatus() == OrderStatus.PLACED || order.getStatus() == OrderStatus.COOKING){
                    actionList.add(new CookOrderAction(order));
                }
            }
        }
        return actionList;
    }
}
