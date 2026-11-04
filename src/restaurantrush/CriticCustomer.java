import java.util.List;
import java.util.Objects;

/**
 * Critic customer: loses 12 satisfaction per waiting turn, pays full price.
 *
 * Special behaviour (documented rule): the Critic always orders the item with
 * the MOST preparation units, to test how well the kitchen copes. Ties go to
 * the higher price, then to the earlier menu position. With the base menu
 * this is always Pasta (2 units).
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
        Objects.requireNonNull(menu, "menu must not be null");
        List<MenuItem> items = menu.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Menu has no items to choose from");
        }
        MenuItem best = items.get(0);
        for (MenuItem item : items) {
            boolean moreUnits = item.getPrepUnits() > best.getPrepUnits();
            boolean sameUnitsDearer = item.getPrepUnits() == best.getPrepUnits()
                    && item.getPriceCents() > best.getPriceCents();
            if (moreUnits || sameUnitsDearer) {
                best = item;
            }
        }
        return best;
    }
}
