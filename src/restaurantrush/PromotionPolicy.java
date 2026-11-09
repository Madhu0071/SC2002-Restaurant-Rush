package restaurantrush;

public interface PromotionPolicy {

    int adjust(Customer c, int cents, int turn);

    void onTurnStart(int turn, Restaurant r);
}
