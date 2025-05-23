package dev.codetoreason.patterns.tactical.rule;

/**
 * Provides a reason string explaining why a given value failed a rule.
 * <p>
 * Used in conjunction with {@link Rule} to generate dynamic or contextual failure messages.
 *
 * @param <T> the type being validated
 */
@FunctionalInterface
public interface FailureReasonFactory<T> {

    /**
     * Generates a failure reason for the given input.
     *
     * @param t the value that failed validation
     * @return a human-readable reason why the value failed
     */
    String create(T t);
}

