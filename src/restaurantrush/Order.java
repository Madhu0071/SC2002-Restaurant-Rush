package restaurantrush;

public class Order {
    private Customer customer;
    private MenuItem item;
    private int priceCents;
    private int unitsDone;
    private int readyOnTurn;
    private OrderStatus status;

    public Order(Customer customer, MenuItem item, int priceCents) {
        this.customer = customer;
        this.item = item;
        this.priceCents = priceCents;
        this.unitsDone = 0;
        this.readyOnTurn = -1;
        this.status = OrderStatus.PLACED;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void advance(int turn) {
        if (status != OrderStatus.PLACED && status != OrderStatus.COOKING){
            return;
        }

        unitsDone++;
        
        if (unitsDone >= item.getPrepUnits()){
            status = OrderStatus.READY;
            readyOnTurn = turn;
        }
        else{
            status = OrderStatus.COOKING;
        }
    }

    public boolean isServable(int turn) {
        return status == OrderStatus.READY && readyOnTurn < turn;
    }

    public void markServed() {
        if (status == OrderStatus.READY){
            status = OrderStatus.SERVED;
        }
    }

    public void markPaid() {
        if (status == OrderStatus.SERVED){
            status = OrderStatus.PAID;
        }
    }

    public void cancel() {
        if (status == OrderStatus.PLACED || status == OrderStatus.COOKING || status == OrderStatus.READY){
            status = OrderStatus.CANCELLED;
        }
    }
}
