package restaurantrush;

/**
 * Lifecycle of a customer, in the order the game moves through it.
 *
 * WAITING_IN_QUEUE -> SEATED -> ORDERED -> EATING -> READY_TO_PAY -> PAID
 * Any of the first three may instead go to LEFT_UNHAPPY (satisfaction reached 0).
 */
public enum CustomerState {
    WAITING_IN_QUEUE,
    SEATED,
    ORDERED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT_UNHAPPY
}
