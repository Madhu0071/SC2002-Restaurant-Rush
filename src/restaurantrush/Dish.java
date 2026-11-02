package restaurantrush;

public class Dish implements MenuItem {
    private String name;
    private int priceCents;
    private int prepUnits;

    public Dish(String name, int priceCents, int prepUnits) {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getPriceCents() {
        return 0;
    }

    @Override
    public int getPrepUnits() {
        return 0;
    }
}
