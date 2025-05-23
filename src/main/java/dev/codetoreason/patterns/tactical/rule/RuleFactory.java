package dev.codetoreason.patterns.tactical.rule;

/**
 * A factory that produces {@link Rule} instances.
 * Typically used to defer rule creation or group reusable rule logic.
 *
 * @param <T> the type being validated
 */
@FunctionalInterface
public interface RuleFactory<T> {
    /**
     * Creates a new rule instance.
     *
     * @return the created rule
     */
    Rule<T> create();
}


