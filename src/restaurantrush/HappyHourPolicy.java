package restaurantrush;

public class HappyHourPolicy implements PromotionPolicy {

    private static final int DISCOUNT_PERCENT = 20;
    private static final int RECOVERY = 15;

    private boolean used = false;
    private int turnsRemaining = 0;
    private int activationTurn = -1;  

    public void activate(int turn) {
        if (used) {
            throw new IllegalStateException("Happy Hour can only be activated once per game");
        }
        used = true;
        activationTurn = turn;
        turnsRemaining = 2;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public int adjust(Customer c, int cents, int turn) {
        boolean active = used && turn >= activationTurn && turn < activationTurn + 2;
        if (!active) {
            return cents;
        }
        return (cents * (100 - DISCOUNT_PERCENT) + 50) / 100;
    }

    @Override
    public void onTurnStart(int turn, Restaurant r) {
        turnsRemaining = used ? Math.max(0, activationTurn + 2 - turn) : 0;
        boolean active = used && turn >= activationTurn && turn < activationTurn + 2;
        if (!active) {
            return;
        }
        for (Customer c : r.getQueue()) {
            if (c.getState() == CustomerState.WAITING_IN_QUEUE) {
                c.satisfaction = Math.min(100, c.satisfaction + RECOVERY);
            }
        }
        for (Table t : r.getTables()) {
            Customer c = t.getOccupant();
            if (c != null && (c.getState() == CustomerState.SEATED
                    || c.getState() == CustomerState.ORDERED)) {
                c.satisfaction = Math.min(100, c.satisfaction + RECOVERY);
            }
        }
    }
}
