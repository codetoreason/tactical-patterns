package dev.codetoreason.patterns.tactical.rule;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A composite of multiple {@link Rule} instances that are applied sequentially to a given value.
 * <p>
 * Evaluation follows a fail-fast strategy: rules are checked in order and execution stops after
 * the first failure is encountered.
 * <p>
 * Use static factory methods {@link #of(Collection)}, {@link #of(Rule[])} or the fluent DSL via
 * {@link #when(Rule)} to create an instance.
 *
 * @param <T> the type being validated
 */
public class Rules<T> {

    private final List<Rule<T>> all;

    /**
     * Constructs a new {@code Rules} object from an immutable list of rules.
     * Use factory methods instead of calling this constructor directly.
     *
     * @param all the rules to apply
     */
    private Rules(List<Rule<T>> all) {
        this.all = all;
    }

    /**
     * Creates a new {@code Rules} instance from a {@link Collection} of {@link Rule} objects.
     * <p>
     * The internal list is immutable and preserves insertion order.
     * <p>
     * If the collection is {@code null} or contains {@code null} elements,
     * an {@link IllegalArgumentException} is thrown.
     * This method does not silently ignore {@code null}s to prevent hidden validation issues.
     *
     * @param rules the collection of rules to apply; must not be {@code null} or contain {@code null} entries
     * @param <T>   the type being validated
     * @return a {@code Rules} instance
     * @throws IllegalArgumentException if the collection or any rule is {@code null}
     */
    public static <T> Rules<T> of(Collection<Rule<T>> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Rules must be non-null");
        }
        var anyRuleIsNull = rules.stream()
                                 .anyMatch(Objects::isNull);
        if (anyRuleIsNull) {
            throw new IllegalArgumentException("Each particular rule must be non-null");
        }
        return new Rules<>(List.copyOf(rules));
    }

    /**
     * Creates a new {@code Rules} instance from a vararg list of {@link Rule} objects.
     * <p>
     * This is useful for quick, inline composition of multiple rules.
     * <p>
     * If the array is {@code null} or contains {@code null} elements,
     * an {@link IllegalArgumentException} is thrown.
     * This method enforces explicit null handling to avoid silent validation failures.
     *
     * @param rules the rules to apply; must not be {@code null} or contain {@code null} entries
     * @param <T>   the type being validated
     * @return a {@code Rules} instance
     * @throws IllegalArgumentException if the array or any rule is {@code null}
     */
    @SafeVarargs
    public static <T> Rules<T> of(Rule<T>... rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Rules must be non-null");
        }
        var anyRuleIsNull = Arrays.stream(rules)
                                  .anyMatch(Objects::isNull);
        if (anyRuleIsNull) {
            throw new IllegalArgumentException("Each particular rule must be non-null");
        }
        return new Rules<>(List.of(rules));
    }


    /**
     * Starts a fluent DSL builder with a single rule.
     *
     * @param rule the first rule
     * @param <T>  the type being validated
     * @return a {@link RulesBuilder} instance
     */
    public static <T> RulesBuilder<T> when(Rule<T> rule) {
        return new RulesBuilder<>(rule);
    }

    /**
     * Starts a fluent DSL builder with a rule produced by a {@link RuleFactory}.
     *
     * @param ruleFactory the rule factory
     * @param <T>         the type being validated
     * @return a {@link RulesBuilder} instance
     */
    public static <T> RulesBuilder<T> when(RuleFactory<T> ruleFactory) {
        return new RulesBuilder<>(ruleFactory.create());
    }

    /**
     * Applies the rules to the given value until one fails.
     * <p>
     * Rules are evaluated in order of declaration. The result of the first failing rule
     * is returned. If all rules pass, a successful result is returned.
     *
     * @param t the value to validate
     * @return a successful result or the result of the first failing rule
     */
    public OperationResult examine(T t) {
        for (var rule : all) {
            var result = rule.check(t);
            if (result.isFailure()) {
                return result;
            }
        }
        return OperationResult.successful();
    }

    /**
     * A fluent DSL builder for composing multiple rules into a {@link Rules} object.
     *
     * @param <T> the type being validated
     */
    public static final class RulesBuilder<T> {

        private final List<Rule<T>> rules = new ArrayList<>();

        /**
         * Initializes the builder with the first rule.
         *
         * @param rule the initial rule
         */
        private RulesBuilder(Rule<T> rule) {
            if (rule == null) {
                throw new IllegalArgumentException("Rule must be non-null");
            }
            rules.add(rule);
        }

        /**
         * Adds another rule to the rule set.
         *
         * @param rule the rule to add
         * @return this builder instance
         */
        public RulesBuilder<T> and(Rule<T> rule) {
            if (rule == null) {
                throw new IllegalArgumentException("Rule must be non-null");
            }
            rules.add(rule);
            return this;
        }

        /**
         * Adds another rule to the rule set using a {@link RuleFactory}.
         *
         * @param ruleFactory the rule factory
         * @return this builder instance
         */
        public RulesBuilder<T> and(RuleFactory<T> ruleFactory) {
            return and(ruleFactory.create());
        }

        /**
         * Finalizes the rule composition and returns an immutable {@link Rules} instance.
         *
         * @return the composed {@link Rules} object
         */
        public Rules<T> compose() {
            return new Rules<>(List.copyOf(rules));
        }
    }
}
