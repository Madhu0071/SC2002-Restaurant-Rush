package restaurantrush;

import java.util.List;
import java.util.OptionalDouble;

public class GameSnapshot {
    private int turn;
    private int revenueCents;
    private OptionalDouble avgSatisfaction;
    private List<Customer> queue;
    private List<Table> tables;
    private int happyHourRemaining;

    public GameSnapshot(int turn, int revenueCents, OptionalDouble avgSatisfaction, List<Customer> queue, List<Table> tables, int happyHourRemaining) {
    }

    public int getTurn() {
        return 0;
    }

    public int getRevenueCents() {
        return 0;
    }

    public OptionalDouble getAvgSatisfaction() {
        return null;
    }

    public List<Customer> getQueue() {
        return null;
    }

    public List<Table> getTables() {
        return null;
    }

    public int getHappyHourRemaining() {
        return 0;
    }
}
