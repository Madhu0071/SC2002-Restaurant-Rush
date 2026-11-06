/** Lifecycle states of a customer (exactly as in the class diagram). */
public enum CustomerState {
    WAITING_IN_QUEUE,
    SEATED,
    ORDERED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT_UNHAPPY
}
