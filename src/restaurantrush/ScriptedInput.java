package restaurantrush;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Queue;

public class ScriptedInput implements ManagerInput {
    private Queue<String> script;

    public ScriptedInput(String file) {
        script = new ArrayDeque<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    script.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read script file: " + file, e); // for missing file, or unable to open
        }
    }

    public StaffAction chooseAction(Staff s, List<StaffAction> opts, GameSnapshot snap) {
        if (script.isEmpty()) {
            throw new IllegalStateException("Script ran out of choices for " + s.getName() + " on turn " + snap.getTurn() + ".");
        }

        String answer = script.remove();
        if (answer.equals("0") || answer.equalsIgnoreCase("WAIT")) {
            return null;
        }

        int choice;
        try {
            choice = Integer.parseInt(answer); // checks if answer is integer
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid script action: " + answer, e);
        }

        if (choice < 1 || choice > opts.size()) {
            throw new IllegalArgumentException("Script chose action " + choice + 
            " for " + s.getName() + " on turn " + snap.getTurn() + 
            ", but only " + opts.size() + " actions are available.");
        }

        StaffAction selected = opts.get(choice - 1);
        return selected;
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
        System.out.println("\n============== [SCRIPT] START TURN " + snap.getTurn() + " ==============");
        System.out.printf("Revenue: $%.2f%n", snap.getRevenueCents() / 100.0);
        OptionalDouble avg = snap.getAvgSatisfaction();
        if (avg.isPresent()) {
            System.out.printf("Average Paid Customers' Satisfaction: %.2f%n", avg.getAsDouble());
        } else {
            System.out.println("Average Paid Customers' Satisfaction: N/A");
        }

        System.out.println("WAITING QUEUE (Top customer = Front of queue):");
        List<Customer> q = snap.getQueue();
        if (q.isEmpty()) {
            System.out.println("No waiting customers.");
        } else {
            for (Customer customer : q) {
                System.out.println("Customer #" + customer.getId());
            }
        }

        System.out.println("\nTABLES:");
        List<Table> tables = snap.getTables();
        for (int i = 0; i < tables.size(); i++) {
            System.out.println("Table " + (i+1) + " - " + (tables.get(i).isFree() ? "FREE" : "OCCUPIED"));
        }

        System.out.println("\n============== CUSTOMER STATUS ==============");
        List<Customer> customers = snap.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
        } else {
            for (Customer customer : customers) {
                System.out.println("\nCustomer #" + customer.getId());
                System.out.println("Satisfaction: " + customer.getSatisfaction());
                System.out.println("State: " + customer.getState());
                if (customer.getOrder() != null) {
                    System.out.println("Order Status: " + customer.getOrder().getStatus());
                } else { System.out.println("Order Status: No order yet."); }
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
        System.out.println("\n============== [SCRIPT] END OF TURN " + r.getTurn() + " ==============");
        System.out.println("Total Customers Served: " + r.getServedCount());
        System.out.println("Total Unhappy Departures: " + r.getUnhappyDepartures());
        System.out.printf("Revenue: $%.2f%n", r.getRevenueCents() / 100.0);

        OptionalDouble avg = r.getAvgSatisfaction();
        if (avg.isPresent()) {
            System.out.printf("Average Paid Customers' Satisfaction: %.2f%n", avg.getAsDouble());
        } else {
            System.out.println("Average Paid Customers' Satisfaction: N/A");
        }

        System.out.println("Turns Used: " + r.getTurn());
    }

    public void showOutcome(GameOutcome o) {
        System.out.println("[script] Final outcome: " + o);
    }

    public void showMessage(String msg) {
        System.out.println("[script] " + msg);
    }
}
