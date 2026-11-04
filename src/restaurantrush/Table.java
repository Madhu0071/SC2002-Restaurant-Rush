package restaurantrush;

public class Table {
    private int id;
    private Customer occupant;

    public Table(int id) {
        this.id = id;
        this.occupant = null;
    }

    public int getId() {
        return id;
    }

    public Customer getOccupant() {
        return occupant;
    }

    public boolean isFree() {
        return occupant == null;
    }

    public void assign(Customer c) {
        if (!isFree()) {
            throw new IllegalStateException("Table " + id + " is already occupied");
        }
        occupant = c;
    }

    public void release() {
        occupant = null;
    }

    @Override
    public String toString() {
        return "Table " + id;
    }
}
