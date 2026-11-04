package restaurantrush;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArrivalSchedule {
    private static final int[] ARRIVAL_TURNS = {1, 4, 7, 10, 13, 16};
    private Map<Integer, Customer> arrivals;

    public ArrivalSchedule() {
        arrivals = new HashMap<>();
        int id = 1;
        for (int turn : ARRIVAL_TURNS) {
            arrivals.put(turn, create(id, turn));
            id++;
        }
    }

    private Customer create(int id, int turn) {
        return new RegularCustomer(id, turn);
    }

    public Optional<Customer> arrivingOn(int turn) {
        return Optional.ofNullable(arrivals.get(turn));
    }
}
