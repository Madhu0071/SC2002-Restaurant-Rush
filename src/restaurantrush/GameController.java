package restaurantrush;

import java.util.List;

public class GameController {
    private static final int TABLE_COUNT = 2;
    private int maxTurns = 18;
    private int turn;
    private ManagerInput input;
    private Restaurant restaurant;
    private PricingService pricing;
    private ArrivalSchedule schedule;
    private ObjectiveEvaluator evaluator;
    private Cashier cashier;

    public GameController(ManagerInput input) {
    }

    public GameOutcome run() {
        return null;
    }

    private TurnReport runTurn() {
        return null;
    }

    private void processArrivals() {
    }

    private void managerChoices() {
    }

    private void askForTask(Staff s) {
    }

    private void eatingAndPayment() {
    }

    private void waitingAndAbandonment() {
    }

    private void depart(Customer c) {
    }

    public GameSnapshot snapshot() {
        return null;
    }
}
