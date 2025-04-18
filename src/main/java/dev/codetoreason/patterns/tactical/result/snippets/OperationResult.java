package dev.codetoreason.patterns.tactical.result.snippets;

class OperationResult {

    private final boolean isSuccess;
    private final String message;

    private OperationResult(boolean isSuccess, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        this.isSuccess = isSuccess;
        this.message = message;
    }

    private static final OperationResult SUCCESSFUL = new OperationResult(true, "OK");

    static OperationResult successful() {
        return SUCCESSFUL;
    }

    static OperationResult failed(String message) {
        return new OperationResult(false, message);
    }

    boolean isSuccess() {
        return isSuccess;
    }

    boolean isFailure() {
        return !isSuccess;
    }

    String message() {
        return message;
    }

}
