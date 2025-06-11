package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.util.function.Predicate;

class Rule<T> {

    private final Predicate<T> predicate;
    private final FailureReasonFactory<T> failureReasonFactory;

    private Rule(Predicate<T> predicate, FailureReasonFactory<T> failureReasonFactory) {
        if (predicate == null || failureReasonFactory == null) {
            throw new IllegalArgumentException("predicate and failureReasonFactory must not be null");
        }
        this.predicate = predicate;
        this.failureReasonFactory = failureReasonFactory;
    }

    static <T> RuleBuilder<T> when(Predicate<T> predicate) {
        return new RuleBuilder<>(predicate);
    }

    OperationResult check(T t) {
        return predicate.test(t)
                ? OperationResult.successful()
                : OperationResult.failed(failureReasonFactory.create(t));
    }

    static final class RuleBuilder<T> {

        private final Predicate<T> predicate;

        private RuleBuilder(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        Rule<T> orElse(String failureReason) {
            if (failureReason == null || failureReason.isBlank()) {
                throw new IllegalArgumentException("Failure reason must be non-null and non-blank");
            }
            return orElse(_ -> failureReason);
        }

        Rule<T> orElse(FailureReasonFactory<T> failureReasonFactory) {
            return new Rule<>(predicate, failureReasonFactory);
        }
    }
}
