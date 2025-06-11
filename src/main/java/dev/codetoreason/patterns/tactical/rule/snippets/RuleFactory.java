package dev.codetoreason.patterns.tactical.rule.snippets;

@FunctionalInterface
interface RuleFactory<T> {

    Rule<T> create();
}


