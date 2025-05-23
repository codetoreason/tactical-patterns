package dev.codetoreason.patterns.tactical.rule;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.util.function.Predicate;

/**
 * Represents a single business rule that validates a value of type {@code T}.
 * <p>
 * A rule encapsulates a {@link Predicate} and a {@link FailureReasonFactory} to determine
 * whether a given value passes validation, and if not, why it failed.
 * <p>
 * Rules should be created via the fluent API:
 * <pre>{@code
 * Rule<String> emailRule = Rule.when(email -> email.contains("@"))
 *                              .orElse("Missing @ symbol");
 * }</pre>
 *
 * @param <T> the type being validated
 */
public final class Rule<T> {

    private final Predicate<T> predicate;
    private final FailureReasonFactory<T> failureReasonFactory;

    /**
     * Creates a new rule with the given predicate and failure reason factory.
     * Use the {@link #when(Predicate)} factory method instead of calling this constructor directly.
     *
     * @param predicate            the predicate to evaluate
     * @param failureReasonFactory the factory to generate failure messages
     * @throws IllegalArgumentException if any argument is null
     */
    private Rule(Predicate<T> predicate, FailureReasonFactory<T> failureReasonFactory) {
        if (predicate == null || failureReasonFactory == null) {
            throw new IllegalArgumentException("predicate and failureReasonFactory must not be null");
        }
        this.predicate = predicate;
        this.failureReasonFactory = failureReasonFactory;
    }

    /**
     * Starts building a rule with the given predicate.
     *
     * @param predicate the validation condition
     * @param <T>       the type being validated
     * @return a builder that expects the failure reason
     */
    public static <T> RuleBuilder<T> when(Predicate<T> predicate) {
        return new RuleBuilder<>(predicate);
    }

    /**
     * Checks the given value against this rule.
     *
     * @param t the value to validate
     * @return a successful {@link OperationResult} if the predicate passes,
     * or a failed result with the failure reason otherwise
     */
    public OperationResult check(T t) {
        return predicate.test(t)
                ? OperationResult.successful()
                : OperationResult.failed(failureReasonFactory.create(t));
    }

    /**
     * A fluent DSL builder for constructing {@link Rule} instances.
     *
     * @param <T> the type being validated
     */
    public static final class RuleBuilder<T> {

        private final Predicate<T> predicate;

        /**
         * Creates a builder with the given predicate.
         *
         * @param predicate the predicate to evaluate
         */
        private RuleBuilder(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        /**
         * Finalizes the rule using a fixed failure message.
         *
         * @param failureReason the reason message when the predicate fails
         * @return a complete {@link Rule} instance
         * @throws IllegalArgumentException if the message is null or blank
         */
        public Rule<T> orElse(String failureReason) {
            if (failureReason == null || failureReason.isBlank()) {
                throw new IllegalArgumentException("FailureReason function must be non-null and non-blank");
            }
            return orElse(_ -> failureReason);
        }

        /**
         * Finalizes the rule using a dynamic failure reason factory.
         *
         * @param failureReasonFactory the factory that generates the failure message
         * @return a complete {@link Rule} instance
         */
        public Rule<T> orElse(FailureReasonFactory<T> failureReasonFactory) {
            return new Rule<>(predicate, failureReasonFactory);
        }
    }
}
