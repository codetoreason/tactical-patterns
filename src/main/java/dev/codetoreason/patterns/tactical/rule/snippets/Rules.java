package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

class Rules<T> {

    private final List<Rule<T>> all;

    private Rules(List<Rule<T>> all) {
        this.all = all;
    }

    static <T> Rules<T> of(Collection<Rule<T>> rules) {
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

    @SafeVarargs
    static <T> Rules<T> of(Rule<T>... rules) {
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

    static <T> RulesBuilder<T> when(Rule<T> rule) {
        return new RulesBuilder<>(rule);
    }

    static <T> RulesBuilder<T> when(RuleFactory<T> ruleFactory) {
        if (ruleFactory == null) {
            throw new IllegalArgumentException("Rule factory must be non-null");
        }
        return new RulesBuilder<>(ruleFactory.create());
    }

    OperationResult examine(T t) {
        for (var rule : all) {
            var result = rule.check(t);
            if (result.isFailure()) {
                return result;
            }
        }
        return OperationResult.successful();
    }

    static final class RulesBuilder<T> {

        private final List<Rule<T>> rules = new ArrayList<>();

        private RulesBuilder(Rule<T> rule) {
            if (rule == null) {
                throw new IllegalArgumentException("Rule must be non-null");
            }
            rules.add(rule);
        }

        RulesBuilder<T> and(Rule<T> rule) {
            if (rule == null) {
                throw new IllegalArgumentException("Rule must be non-null");
            }
            rules.add(rule);
            return this;
        }

        RulesBuilder<T> and(RuleFactory<T> ruleFactory) {
            if (ruleFactory == null) {
                throw new IllegalArgumentException("Rule factory must be non-null");
            }
            return and(ruleFactory.create());
        }

        Rules<T> compose() {
            return new Rules<>(List.copyOf(rules));
        }
    }
}
