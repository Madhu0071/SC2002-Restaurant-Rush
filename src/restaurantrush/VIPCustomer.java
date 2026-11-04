/** VIP customer: loses 5 satisfaction per waiting turn and gets 10% off the bill. */
public class VIPCustomer extends Customer {

    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int waitLoss() {
        return 5;
    }

    /** Bill x 0.9, rounded half-up to the nearest cent with integer maths (no overflow). */
    @Override
    public int adjustBill(int cents) {
        int validated = super.adjustBill(cents);
        return (int) (((long) validated * 9 + 5) / 10);
    }
}
