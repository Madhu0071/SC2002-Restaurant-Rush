package restaurantrush;

public class ValidationResult {
    private boolean valid;
    private String message;

    private ValidationResult(boolean valid, String message) {
    }

    public static ValidationResult ok() {
        return null;
    }

    public static ValidationResult fail(String msg) {
        return null;
    }

    public boolean isValid() {
        return false;
    }

    public String getMessage() {
        return null;
    }
}
