package restaurantrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class GameSnapshot {
    private int turn;
    private int revenueCents;
    private OptionalDouble avgSatisfaction;
    private List<Customer> queue;
    private List<Table> tables;
    private List<Customer> customers;
    
    public GameSnapshot(int turn, int revenueCents, OptionalDouble avgSatisfaction, List<Customer> queue,
                        List<Table> tables, List<Customer> customers) {
        this.turn = turn;
        this.revenueCents = revenueCents;
        this.avgSatisfaction = avgSatisfaction;
        this.queue = Collections.unmodifiableList(new ArrayList<>(queue));
        this.tables = Collections.unmodifiableList(new ArrayList<>(tables));
        this.customers = Collections.unmodifiableList(new ArrayList<>(customers));
        
    }

    public int getTurn() {
        return turn;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public OptionalDouble getAvgSatisfaction() {
        return avgSatisfaction;
    }

    public List<Customer> getQueue() {
        return queue;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

}
