package dev.codetoreason.patterns.tactical.rule

import spock.lang.Specification

class RulesSpec extends Specification {

    def "should return success when no rules are defined"() {
        expect:
            Rules.of().examine("any").isSuccess()
    }

    def "should return OK message when success"() {
        expect:
            Rules.of().examine("any").message() == "OK"
    }

    def "should return success when rules list is empty"() {
        expect:
            Rules.of([]).examine("input").isSuccess()
    }

    def "should return success when single rule passes"() {
        given:
            def rule = Rule.when((int i) -> i > 0)
                           .orElse(i -> "Must be positive")
            def rules = Rules.of(rule)

        expect:
            rules.examine(5).isSuccess()
    }

    def "should return failure when single rule fails"() {
        given:
            def rule = Rule.when((int i) -> i > 0)
                           .orElse(i -> "Must be positive")
            def rules = Rules.of(rule)

        when:
            def result = rules.examine(-3)

        then:
            result.isFailure()
            result.message() == "Must be positive"
    }

    def "should return success when all rules pass"() {
        given:
            def r1 = Rule.when((int i) -> i > 0)
                         .orElse(i -> "positive")
            def r2 = Rule.when((int i) -> i < 100)
                         .orElse(i -> "< 100")
            def rules = Rules.of(r1, r2)

        expect:
            rules.examine(10).isSuccess()
    }

    def "should support fluent composition of multiple rules"() {
        given:
            def rules = Rules.when(
                    Rule.when((int i) -> i > 0)
                        .orElse("gt 0")
            ).and(
                    Rule.when((int i) -> i < 100)
                        .orElse("lt 100")
            ).and(
                    Rule.when((int i) -> i % 2 == 0)
                        .orElse("even")
            ).compose()


        expect:
            rules.examine(42).isSuccess()
            rules.examine(43).isFailure()
    }

    def "should return first failure when multiple rules fail - first fails"() {
        given:
            def r1 = Rule.when((int i) -> i > 10)
                         .orElse(i -> "must be > 10")
            def r2 = Rule.when((int i) -> i < 100)
                         .orElse(i -> "must be < 100")
            def rules = Rules.of(r1, r2)

        when:
            def result = rules.examine(5)

        then:
            result.isFailure()
            result.message() == "must be > 10"
    }

    def "should not evaluate further rules after first failure"() {
        given:
            def failing = Rule.when((int i) -> false)
                              .orElse("fail-fast")
            def heavy = Mock(Rule)
            def rules = Rules.of(failing, heavy)

        when:
            def result = rules.examine(123)

        then:
            result.isFailure()
            0 * heavy.check(_)
    }

    def "should return second failure if first passes and second fails"() {
        given:
            def r1 = Rule.when((int i) -> i > 0)
                         .orElse(i -> "positive")
            def r2 = Rule.when((int i) -> i < 100)
                         .orElse(i -> "< 100")
            def rules = Rules.of(r1, r2)

        when:
            def result = rules.examine(500)

        then:
            result.isFailure()
            result.message() == "< 100"
    }

    def "should work with rule created via RuleFactory"() {
        given:
            RuleFactory<Integer> factory = () -> Rule.when((int i) -> i % 2 == 0)
                                                     .orElse(i -> "Must be even")
            def rules = Rules.of(factory.create())

        expect:
            rules.examine(4).isSuccess()
            rules.examine(5).isFailure()
    }

    def "should compose rules using DSL"() {
        given:
            def rules = Rules.when(
                    Rule.when((int i) -> i > 0)
                        .orElse(i -> "positive")
            ).and(
                    Rule.when((int i) -> i < 100)
                        .orElse(i -> "< 100")
            ).compose()

        expect:
            rules.examine(42).isSuccess()
            rules.examine(0).isFailure()
    }

    def "should compose rules using DSL with RuleFactory"() {
        given:
            def mustBePositive = () -> Rule.when((int i) -> i > 0)
                                           .orElse("Must be positive")
            def mustBeEven = () -> Rule.when((int i) -> i % 2 == 0)
                                       .orElse("Must be even")
            def rules = Rules.when(mustBePositive)
                             .and(mustBeEven)
                             .compose()

        expect:
            rules.examine(2).isSuccess()
            rules.examine(1).isFailure()
            rules.examine(0).isFailure()
    }

    def "should throw when collection is null"() {
        when:
            Rules.of((Collection) null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Rules must be non-null"
    }

    def "should throw when collection contains null"() {
        given:
            def validRule = Rule.when((String s) -> true)
                                .orElse("Should never fail")

        when:
            Rules.of([validRule, null])

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Each particular rule must be non-null"
    }

    def "should throw when varargs is null"() {
        when:
            Rules.of((Rule[]) null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Rules must be non-null"
    }

    def "should throw when varargs contains null"() {
        given:
            def validRule = Rule.when((String s) -> true)
                                .orElse("Should never fail")

        when:
            Rules.of(validRule, null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Each particular rule must be non-null"
    }

    def "should return success when no rules (empty varargs)"() {
        expect:
            Rules.of().examine("input").isSuccess()
    }


    def "should build rules using mixed Rule and RuleFactory in DSL"() {
        given:
            def rules = Rules.when(
                    Rule.when((int i) -> i > 0)
                        .orElse("Too small")
            ).and(
                    () -> Rule.when((int i) -> i < 10)
                              .orElse("Too big")
            ).compose()

        expect:
            rules.examine(5).isSuccess()
            rules.examine(15).isFailure()
    }

    def "should create immutable list of rules in compose()"() {
        given:
            def ruleList = new ArrayList<Rule>()
            ruleList.add(
                    Rule.when((int i) -> i >= 0)
                        .orElse("must not be negative")
            )
            def rules = Rules.of(ruleList)

        when:
            ruleList.add(
                    Rule.when((int i) -> i > 0)
                        .orElse("must be positive")
            )

        then:
            rules.examine(0).isSuccess()
        and:
            ruleList.size() == 2
            Rules.of(ruleList).examine(0).isFailure()
    }
}
