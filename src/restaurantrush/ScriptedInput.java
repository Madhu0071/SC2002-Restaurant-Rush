package restaurantrush;

import java.util.List;
import java.util.Queue;

public class ScriptedInput implements ManagerInput {
    private Queue<String> script;

    public ScriptedInput(String file) {
    }

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
