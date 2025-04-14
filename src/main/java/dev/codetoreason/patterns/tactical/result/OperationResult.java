package dev.codetoreason.patterns.tactical.result;

import java.util.Objects;

/**
 * Represents the result of an operation that does not produce a return value.
 * <p>
 * This is a tactical pattern used to replace traditional exception-based control flow
 * for operations where the outcome is either success or failure, and only a descriptive message is needed.
 * <p>
 * This class is immutable, null-safe, and semantically explicit.
 * Use {@link #successful()} for success, and {@link #failed(String)} for failure cases.
 */
public class OperationResult {

    private final boolean isSuccess;
    private final String message;

    private static final OperationResult SUCCESSFUL = new OperationResult(true, "OK");

    /**
     * Private constructor to enforce usage of static factory methods.
     *
     * @param isSuccess whether the operation succeeded
     * @param message   the descriptive message (must not be null or blank)
     * @throws IllegalArgumentException if message is null or blank
     */
    private OperationResult(boolean isSuccess, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        this.isSuccess = isSuccess;
        this.message = message;
    }

    /**
     * Returns the singleton instance representing a successful result.
     *
     * @return an {@code OperationResult} indicating success
     */
    public static OperationResult successful() {
        return SUCCESSFUL;
    }

    /**
     * Creates a new failure result with a descriptive message.
     *
     * @param message the failure message (must not be null or blank)
     * @return a new {@code OperationResult} representing failure
     * @throws IllegalArgumentException if message is null or blank
     */
    public static OperationResult failed(String message) {
        return new OperationResult(false, message);
    }

    /**
     * Creates a new failure result with a formatted message.
     *
     * @param messageTemplate a format string (e.g. "Error: %s")
     * @param args            arguments to fill into the format string
     * @return a new {@code OperationResult} representing failure
     * @throws IllegalArgumentException if the resulting message is null or blank
     */
    public static OperationResult failed(String messageTemplate, Object... args) {
        return failed(messageTemplate.formatted(args));
    }

    /**
     * Returns whether the operation was successful.
     *
     * @return {@code true} if the result is success; {@code false} otherwise
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns whether the operation failed.
     *
     * @return {@code true} if the result is failure; {@code false} otherwise
     */
    public boolean isFailure() {
        return !isSuccess;
    }

    /**
     * Returns the message associated with this result.
     * For success, this is typically "OK".
     * For failure, this should describe the reason for failure.
     *
     * @return the message (never null or blank)
     */
    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OperationResult that)) return false;
        return isSuccess == that.isSuccess && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSuccess, message);
    }

    /**
     * Returns a human-readable representation of the result.
     * Example: {@code OperationResult{isSuccess=false, message='Validation error'}}
     */
    @Override
    public String toString() {
        return "OperationResult{" +
                "isSuccess=" + isSuccess +
                ", message='" + message + '\'' +
                '}';
    }
}
