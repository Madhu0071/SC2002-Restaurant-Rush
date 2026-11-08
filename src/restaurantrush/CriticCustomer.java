package restaurantrush;

import java.util.List;

/**
 * A food critic: loses 12 satisfaction per waiting turn and pays full price.
 *
 * Order preference: always orders the most expensive item on the menu. If
 * two items share the top price, the one listed first on the menu is chosen.
 *
 * Reaction to service: when the critic pays, they review the speed of
 * service. If their food was served within 3 turns of arriving
 * (servedOnTurn - arrivalTurn <= 3), they gain 10 satisfaction; otherwise
 * they lose 10. The result is kept within 0-100. The review only applies
 * once the critic is PAID, and the controller calls onPaid() once per payment.
 */
public class CriticCustomer extends Customer {

    public CriticCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int waitLoss() {
        return 12;
    }

    @Override
    public MenuItem chooseItem(Menu menu, int turn) {
        List<MenuItem> items = menu.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Menu has no items to choose from");
        }
        MenuItem best = items.get(0);
        for (MenuItem item : items) {
            if (item.getPriceCents() > best.getPriceCents()) {
                best = item;
            }
        }
        return best;
    }

    @Override
    public void onPaid(Order o) {
        if (getState() != CustomerState.PAID) {
            return;
        }
        boolean servedFast = getServedOnTurn() >= 0
                && getServedOnTurn() - getArrivalTurn() <= 3;
        if (servedFast) {
            changeSatisfaction(10);
        } else {
            changeSatisfaction(-10);
        }
    }
}
