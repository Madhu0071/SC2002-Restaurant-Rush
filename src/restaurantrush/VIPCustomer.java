package restaurantrush;

public class VIPCustomer extends Customer {

    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int waitLoss() {
        return 5;
    }

    @Override
    public int adjustBill(int cents) {
        return (cents * 90 + 50) / 100;
    }
}
