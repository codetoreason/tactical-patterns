package dev.codetoreason.patterns.tactical.result;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the outcome of an operation that returns a value upon success, or a descriptive message upon failure.
 * <p>
 * This is a tactical alternative to exception-based flow control, designed to make operation results
 * more explicit, testable, and intention-revealing.
 * <p>
 * A {@code Result<T>} is either:
 * <ul>
 *     <li>{@code success}, holding a non-null {@code value}, and a default message "OK"</li>
 *     <li>{@code failure}, holding a {@code null} value, and a descriptive message of the error</li>
 * </ul>
 *
 * <p>This class is:
 * <ul>
 *     <li>Immutable</li>
 *     <li>Null-safe (validated via constructor)</li>
 *     <li>Semantically explicit</li>
 *     <li>Aligned with the principle of Business-Oriented Programming</li>
 * </ul>
 *
 * @param <T> the type of the value returned on success
 * @see dev.codetoreason.patterns.tactical.result.OperationResult for operations that do not return a value
 */
public class Result<T> {

    private final T value;
    private final String message;

    /**
     * Creates a new {@code Result<T>} with the given value and message.
     * <p>
     * Private to enforce use of static factory methods.
     *
     * @param value   the value (can be {@code null} in failure case)
     * @param message the message (never null or blank)
     * @throws IllegalArgumentException if the message is null or blank
     */
    private Result(T value, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        this.value = value;
        this.message = message;
    }

    /**
     * Creates a successful result with the provided non-null value.
     * The message is set to "OK".
     *
     * @param value the non-null value to return
     * @param <T>   the type of the value
     * @return a {@code Result<T>} indicating success
     * @throws IllegalArgumentException if the value is null
     */
    public static <T> Result<T> successful(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null for successful result");
        }
        return new Result<>(value, "OK");
    }

    /**
     * Creates a failed result with a descriptive message.
     *
     * @param message the error message (must not be null or blank)
     * @param <T>     the result type (will hold no value)
     * @return a {@code Result<T>} indicating failure
     * @throws IllegalArgumentException if the message is null or blank
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(null, message);
    }

    /**
     * Creates a failed result with a formatted message.
     *
     * @param messageTemplate a format string (e.g. "Invalid input: %s")
     * @param args            arguments to fill into the format string
     * @param <T>             the result type (will hold no value)
     * @return a {@code Result<T>} indicating failure
     * @throws IllegalArgumentException if the resulting message is null or blank
     */
    public static <T> Result<T> failed(String messageTemplate, Object... args) {
        return failed(messageTemplate.formatted(args));
    }

    /**
     * Returns the value wrapped by this result.
     * <p>
     * If the result is a failure, the returned {@link Optional} will be empty.
     *
     * @return an {@link Optional} containing the value, or empty if failure
     */
    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    /**
     * Indicates whether the result is a success.
     *
     * @return {@code true} if the result has a non-null value
     */
    public boolean isSuccess() {
        return value != null;
    }

    /**
     * Indicates whether the result is a failure.
     *
     * @return {@code true} if the result has a null value
     */
    public boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns the descriptive message associated with this result.
     * <p>
     * For success, this is typically "OK".
     * For failure, this is the reason for failure.
     *
     * @return a non-null, non-blank message
     */
    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Result<?> result)) return false;
        return Objects.equals(value, result.value) && Objects.equals(message, result.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, message);
    }

    /**
     * Returns a readable string representation of this result, useful for logging and debugging.
     * Example:
     * <pre>
     * Result{result=SUCCESS, value=User123, message='OK'}
     * Result{result=FAILURE, value=null, message='Validation error'}
     * </pre>
     *
     * @return a formatted string describing the result
     */
    @Override
    public String toString() {
        return "Result{" +
                "result=" + (isSuccess() ? "SUCCESS" : "FAILURE") + ", " +
                "value=" + (value != null ? value.toString() : "null") + ", " +
                "message='" + message + '\'' +
                '}';
    }
}
