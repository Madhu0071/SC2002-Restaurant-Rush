package restaurantrush;

import java.io.PrintStream;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class RestaurantCLI implements ManagerInput {
    private Scanner in;
    private PrintStream out;

    public RestaurantCLI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public StaffAction chooseAction(Staff s, List<StaffAction> opts, GameSnapshot snap) {
        out.println("\nTurn " + snap.getTurn() + " - Available actions for " + s.getName() + ":");

        out.println("0 - Wait");
        for (int i = 0; i < opts.size(); i++) {
            out.println((i+1) + " - " + opts.get(i).describe());
        }

        while (true) {
            out.print("Enter your choice: ");
            if (!in.hasNextLine()) {
                throw new IllegalStateException("Input ended while choosing a staff action.");
            }
            String answer = in.nextLine().trim();
            try {
                int choice = Integer.parseInt(answer);
                if (choice == 0) {
                    return null;
                } else if (choice >= 1 && choice <= opts.size()) {
                    return opts.get(choice - 1);
                }
            } catch (NumberFormatException e) {} // displays error message below and asks for input again
            out.println("Invalid choice. Enter a number from 0 to " + opts.size() + ".");
        }

    }

    public void showTurnStart(GameSnapshot snap) {
        /* to display at START OF TURN:
        1. turn number
        2. revenue
        3. avg satisfaction of paid customers
        4. waiting queue
        5. occupied tables
        6. each customer's satisfaction
        7. order status
        8. staff tasks available
        */
        out.println("\n============== START TURN " + snap.getTurn() + " ==============");
        out.printf("REVENUE: $%.2f%n", snap.getRevenueCents() / 100.0);
        OptionalDouble avg = snap.getAvgSatisfaction();
        if (avg.isPresent()) {
            out.printf("AVERAGE PAID CUSTOMERS' SATISFACTION: %.2f%n", avg.getAsDouble());
        } else {
            out.println("AVERAGE PAID CUSTOMERS' SATISFACTION: N/A");
        }

        out.println("WAITING QUEUE (TOP CUSTOMER = FRONT OF QUEUE):");
        List<Customer> q = snap.getQueue();
        if (q.isEmpty()) {
            out.println("No waiting customers.");
        } else {
            for (Customer customer : q) {
                out.println("Customer #" + customer.getId());
            }
        }

        out.println("\nTABLES:");
        List<Table> tables = snap.getTables();
        for (int i = 0; i < tables.size(); i++) {
            out.println("Table " + (i+1) + " - " + (tables.get(i).isFree() ? "FREE" : "OCCUPIED"));
        }

        out.println("\n----EACH CUSTOMER'S SATISFACTION AND STATUS----");
        List<Customer> customers = snap.getCustomers();
        if (customers.isEmpty()) {
            out.println("No customers available.");
        } else {
            for (Customer customer : customers) {
                out.println("\nCustomer #" + customer.getId());
                out.println("SATISFACTION: " + customer.getSatisfaction());
                out.print("STATE: ");
                switch (customer.getState()) {
                    case WAITING_IN_QUEUE:
                        out.println("Waiting in queue");
                        break;
                    case SEATED:
                        out.println("Seated");
                        break;
                    case ORDERED:
                        out.println("Ordered");
                        break;
                    case EATING:
                        out.println("Eating");
                        break;
                    case READY_TO_PAY:
                        out.println("Ready to pay");
                        break;
                    case PAID:
                        out.println("Paid");
                        break;
                    case LEFT_UNHAPPY:
                        out.println("Left Unhappy");
                        break;
                    default:
                        break;
                }

                if (customer.getOrder() != null) {
                    out.println("ORDER STATUS: " + customer.getOrder().getStatus());
                } else { out.println("ORDER STATUS: No order yet."); }
            }
        }
    }

    public void showTurnReport(TurnReport r) {
        /* to display in TURN REPORT:
        1. served count
        2. unhappy departures
        3. revenue
        4. average satisfaction of paid customers
        - N/A if nobody has paid yet
        5. turns used
        */
        out.println("\n============== END OF TURN " + r.getTurn() + " ==============");
        out.println("TOTAL CUSTOMERS SERVED: " + r.getServedCount());
        out.println("TOTAL UNHAPPY DEPARTURES: " + r.getUnhappyDepartures());
        out.printf("REVENUE: $%.2f%n", r.getRevenueCents() / 100.0);

        OptionalDouble avg = r.getAvgSatisfaction();
        if (avg.isPresent()) {
            out.printf("AVERAGE PAID CUSTOMERS' SATISFACTION: %.2f%n", avg.getAsDouble());
        } else {
            out.println("AVERAGE PAID CUSTOMERS' SATISFACTION: N/A");
        }

        out.println("TURNS USED: " + r.getTurn());
    }

    public void showOutcome(GameOutcome o) {
        out.println("============== END OF GAME ==============");
        if (o == GameOutcome.VICTORY) {
            out.println("All 3 objectives were met.");
            out.println("FINAL OUTCOME: VICTORY!");
        } else {
            out.println("Not all objectives were met.");
            out.println("FINAL OUTCOME: DEFEAT.");
        }
    }

    public void showMessage(String msg) {
        out.println(msg);
    }
}
