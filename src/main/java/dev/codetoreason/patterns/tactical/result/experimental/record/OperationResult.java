package dev.codetoreason.patterns.tactical.result.experimental.record;

record OperationResult(
        boolean isSuccess,
        String message
) {

    private static final OperationResult SUCCESSFUL = new OperationResult(true, "OK");

    OperationResult {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
    }

    static OperationResult successful() {
        return SUCCESSFUL;
    }

    static OperationResult failed(String message) {
        return new OperationResult(false, message);
    }

    static OperationResult failed(String messageTemplate, Object... args) {
        return failed(messageTemplate.formatted(args));
    }

    boolean isFailure() {
        return !isSuccess;
    }
}
