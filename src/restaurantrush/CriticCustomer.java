package restaurantrush;

import java.util.List;

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
        int review = servedFast ? 10 : -10;
        satisfaction = Math.max(0, Math.min(100, satisfaction + review));
        
    }
}
