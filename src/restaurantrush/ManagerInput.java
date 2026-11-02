package restaurantrush;

import java.util.List;

public interface ManagerInput {
    StaffAction chooseAction(Staff s, List<StaffAction> opts, GameSnapshot snap);

    boolean confirmHappyHour(GameSnapshot snap);

    void showTurnStart(GameSnapshot snap);

    void showTurnReport(TurnReport r);

    void showOutcome(GameOutcome o);

    void showMessage(String msg);
}
