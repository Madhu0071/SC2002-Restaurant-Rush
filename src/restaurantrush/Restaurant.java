package restaurantrush;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.OptionalDouble;

public class Restaurant {
    private List<Table> tables;
    private Deque<Customer> queue;
    private List<Staff> staff;
    private List<Customer> customers;
    private Menu menu;
    private PricingService pricing;
    private int revenueCents;
    private int paidCount;
    private int satisfactionSum;

    public Restaurant(int tableCount, Menu menu, PricingService pricing) {
        this.tables = new ArrayList<>();
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(i));
        }
        this.queue = new ArrayDeque<>();
        this.staff = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.menu = menu;
        this.pricing = pricing;
        this.revenueCents = 0;
        this.paidCount = 0;
        this.satisfactionSum = 0;
    }

    public void enqueue(Customer c) {
        queue.addLast(c);
        customers.add(c);
    }

    public Customer nextQueued() {
        return queue.peekFirst();
    }

    public boolean removeFromQueue(Customer c) {
        return queue.remove(c);
    }

    public List<Customer> getQueue() {
        return new ArrayList<>(queue);
    }

    public Table freeTable() {
        for (Table t : tables) {
            if (t.isFree()) {
                return t;
            }
        }
        return null;
    }

    public Table tableOf(Customer c) {
        for (Table t : tables) {
            if (t.getOccupant() == c) {
                return t;
            }
        }
        return null;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public void registerStaff(Staff s) {
        staff.add(s);
    }

    public List<Staff> getStaff() {
        return Collections.unmodifiableList(staff);
    }

    public Menu getMenu() {
        return menu;
    }

    public PricingService getPricing() {
        return pricing;
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public void recordPayment(Customer c, int cents) {
        revenueCents += cents;
        paidCount++;
        satisfactionSum += c.getSatisfaction();
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public OptionalDouble averageSatisfaction() {
        if (paidCount == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of((double) satisfactionSum / paidCount);
    }

    public int getServedCount() {
        int count = 0;
        for (Customer c : customers) {
            CustomerState s = c.getState();
            if (s == CustomerState.EATING || s == CustomerState.READY_TO_PAY || s == CustomerState.PAID) {
                count++;
            }
        }
        return count;
    }

    public int getUnhappyDepartures() {
        int count = 0;
        for (Customer c : customers) {
            if (c.getState() == CustomerState.LEFT_UNHAPPY) {
                count++;
            }
        }
        return count;
    }
}
