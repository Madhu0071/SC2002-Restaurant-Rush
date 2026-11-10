package restaurantrush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private HappyHourPolicy happyHour;
    private List<PromotionPolicy> policies;

    public GameController(ManagerInput input) {
        this.input = input;
        this.turn = 0;
        this.pricing = new PricingService();
        Dish salad = new Dish("Salad", 900, 1);
        Dish burger = new Dish("Burger", 1500, 1);
        Dish pasta = new Dish("Pasta", 1800, 2);
        List<MenuItem> items = Arrays.asList(
                salad,
                burger,
                pasta,
                new ComboMeal(salad, pasta),
                new ComboMeal(burger, salad));
        this.restaurant = new Restaurant(TABLE_COUNT, new Menu(items), pricing);
        restaurant.registerStaff(new Host());
        restaurant.registerStaff(new Waiter());
        restaurant.registerStaff(new Chef());
        this.cashier = new Cashier();
        restaurant.registerStaff(cashier);
        this.schedule = new ArrivalSchedule();
        this.evaluator = new ObjectiveEvaluator();
        this.happyHour = new HappyHourPolicy();
        this.policies = new ArrayList<>();
        policies.add(happyHour);
        pricing.addPolicy(happyHour);
    }

    public GameOutcome run() {
        while (turn < maxTurns) {
            TurnReport report = runTurn();
            input.showTurnReport(report);
        }
        GameOutcome outcome = evaluator.evaluate(restaurant);
        input.showOutcome(outcome);
        return outcome;
    }

    private TurnReport runTurn() {
        turn++;
        processArrivals();
        input.showTurnStart(snapshot());
        startTurnPolicies();
        managerChoices();
        eatingAndPayment();
        waitingAndAbandonment();
        return new TurnReport(turn, restaurant.getServedCount(), restaurant.getUnhappyDepartures(),
                restaurant.getRevenueCents(), restaurant.averageSatisfaction());
    }

    private void processArrivals() {
        Optional<Customer> arrival = schedule.arrivingOn(turn);
        if (arrival.isPresent()) {
            Customer c = arrival.get();
            restaurant.enqueue(c);
            input.showMessage("Customer #" + c.getId() + " arrives and joins the queue.");
        }
    }

    private void startTurnPolicies() {
        if (!happyHour.isUsed() && input.confirmHappyHour(snapshot())) {
            happyHour.activate(turn);
            input.showMessage("Happy Hour is on for this turn and the next.");
        }
        for (PromotionPolicy p : policies) {
            p.onTurnStart(turn, restaurant);
        }
    }

    private void managerChoices() {
        for (Staff s : restaurant.getStaff()) {
            askForTask(s);
        }
    }

    private void askForTask(Staff s) {
        while (true) {
            List<StaffAction> options = s.availableActions(restaurant, turn);
            if (options == null || options.isEmpty()) {
                return;
            }
            StaffAction choice = input.chooseAction(s, options, snapshot());
            if (choice == null) {
                input.showMessage(s.getName() + " waits.");
                return;
            }
            ValidationResult result = choice.validate(restaurant, turn);
            if (result != null && result.isValid()) {
                choice.execute(restaurant, turn);
                input.showMessage(s.getName() + ": " + choice.describe());
                return;
            }
            String reason = (result == null) ? "That choice is not allowed." : result.getMessage();
            input.showMessage("Not allowed: " + reason + " Choose again.");
        }
    }

    private void eatingAndPayment() {
        for (Customer c : restaurant.getCustomers()) {
            if (c.getState() == CustomerState.EATING && c.getServedOnTurn() < turn) {
                c.finishEating(turn);
            }
        }
        Optional<Customer> paid = cashier.collectPayment(restaurant, turn);
        if (paid != null && paid.isPresent()) {
            input.showMessage("Cashier: customer #" + paid.get().getId() + " paid.");
        }
    }

    private void waitingAndAbandonment() {
        for (Customer c : new ArrayList<>(restaurant.getCustomers())) {
            CustomerState s = c.getState();
            boolean waiting = s == CustomerState.WAITING_IN_QUEUE
                    || s == CustomerState.SEATED
                    || s == CustomerState.ORDERED;
            if (!waiting) {
                continue;
            }
            c.loseSatisfaction();
            if (c.getSatisfaction() == 0) {
                depart(c);
            }
        }
    }

    private void depart(Customer c) {
        Order order = c.getOrder();
        if (order != null) {
            order.cancel();
        }
        Table table = restaurant.tableOf(c);
        if (table != null) {
            table.release();
        }
        restaurant.removeFromQueue(c);
        c.leave();
        input.showMessage("Customer #" + c.getId() + " left unhappy without paying.");
    }

    public GameSnapshot snapshot() {
        return new GameSnapshot(turn, restaurant.getRevenueCents(), restaurant.averageSatisfaction(),
                restaurant.getQueue(), restaurant.getTables(), restaurant.getCustomers(),
                happyHour.getTurnsRemaining());
    }
}