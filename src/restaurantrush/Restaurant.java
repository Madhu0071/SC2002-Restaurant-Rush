package restaurantrush;

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
    }

    public void enqueue(Customer c) {
    }

    public Customer nextQueued() {
        return null;
    }

    public boolean removeFromQueue(Customer c) {
        return false;
    }

    public List<Customer> getQueue() {
        return null;
    }

    public Table freeTable() {
        return null;
    }

    public Table tableOf(Customer c) {
        return null;
    }

    public List<Table> getTables() {
        return null;
    }

    public void registerStaff(Staff s) {
    }

    public List<Staff> getStaff() {
        return null;
    }

    public Menu getMenu() {
        return null;
    }

    public PricingService getPricing() {
        return null;
    }

    public List<Customer> getCustomers() {
        return null;
    }

    public void recordPayment(Customer c, int cents) {
    }

    public int getRevenueCents() {
        return 0;
    }

    public int getPaidCount() {
        return 0;
    }

    public OptionalDouble averageSatisfaction() {
        return null;
    }

    public int getServedCount() {
        return 0;
    }

    public int getUnhappyDepartures() {
        return 0;
    }
}
