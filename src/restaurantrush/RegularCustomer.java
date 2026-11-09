package restaurantrush;

public class RegularCustomer extends Customer {

    public RegularCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int waitLoss() {
        return 8;
    }
}
