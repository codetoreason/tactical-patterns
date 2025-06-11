package dev.codetoreason.patterns.tactical.rule.snippets;

@FunctionalInterface
interface FailureReasonFactory<T> {

    String create(T t);
}

