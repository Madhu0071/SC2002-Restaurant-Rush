package restaurantrush;

public class ComboMeal implements MenuItem {
    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Both combo items must be provided.");
        }
        this.first = first;
        this.second = second;
    }

    @Override
    public String getName() {
        return "Combo (" + first.getName() + " + " + second.getName() + ")";
    }

    @Override
    public int getPriceCents() {
        int combinedPrice = first.getPriceCents() + second.getPriceCents();
        return (combinedPrice * 90 + 50) / 100;
    }

    public int getPrepUnits() {
        return first.getPrepUnits() + second.getPrepUnits();
    }
}
