package restaurantrush;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class RestaurantCLI implements ManagerInput {
    private Scanner in;
    private PrintStream out;

    public StaffAction chooseAction(Staff s, List<StaffAction> opts, GameSnapshot snap) {
        return null;
    }

    public boolean confirmHappyHour(GameSnapshot snap) {
        return false;
    }

    public void showTurnStart(GameSnapshot snap) {
    }

    public void showTurnReport(TurnReport r) {
    }

    public void showOutcome(GameOutcome o) {
    }

    public void showMessage(String msg) {
    }
}
