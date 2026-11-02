package restaurantrush;

public interface StaffAction {
    String describe();

    ValidationResult validate(Restaurant r, int turn);

    void execute(Restaurant r, int turn);
}
