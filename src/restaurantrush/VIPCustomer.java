package restaurantrush;

/**
 * A VIP customer: loses 5 satisfaction per waiting turn and gets 10% off the
 * bill, rounded to the nearest cent.
 */
public class VIPCustomer extends Customer {

    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int waitLoss() {
        return 5;
    }

    /**
     * round(cents x 0.9), done in integer arithmetic (half rounds up) to
     * avoid floating-point error.
     */
    @Override
    public int adjustBill(int cents) {
        return (cents * 90 + 50) / 100;
    }
}
