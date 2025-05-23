package dev.codetoreason.patterns.tactical.rule

import spock.lang.Specification
import spock.lang.Unroll

class RuleSpec extends Specification {

    def "should return successful result when predicate passes"() {
        given:
            def rule = Rule.when((int i) -> i > 0)
                           .orElse("Must be positive")

        expect:
            rule.check(5).isSuccess()
    }

    def "should return failed result when predicate fails"() {
        given:
            def rule = Rule.when((int i) -> i > 0)
                           .orElse("Must be positive")

        when:
            def result = rule.check(-3)

        then:
            result.isFailure()
            result.message() == "Must be positive"
    }

    def "should use dynamic failure reason when predicate fails"() {
        given:
            def rule = Rule.when((String s) -> s.length() > 3)
                           .orElse(s -> "Too short: " + s)

        when:
            def result = rule.check("ab")

        then:
            result.isFailure()
            result.message() == "Too short: ab"
    }

    def "should not call failure reason factory when predicate passes"() {
        given:
            def reasonFactoryMock = Mock(FailureReasonFactory)
            def rule = Rule.when((int i) -> i > 10)
                           .orElse(reasonFactoryMock)

        when:
            def result = rule.check(42)

        then:
            result.isSuccess()
            0 * reasonFactoryMock.create(_)
    }

    def "should throw when predicate is null"() {
        when:
            Rule.when(null)
                .orElse("fail")

        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "should throw when static failure reason is invalid: '#reason'"() {
        when:
            Rule.when(_ -> true)
                .orElse(reason)

        then:
            thrown(IllegalArgumentException)

        where:
            reason << [null, "", "   "]
    }

    def "should throw when dynamic failure reason factory is null"() {
        when:
            Rule.when(_ -> false)
                .orElse(null)

        then:
            thrown(IllegalArgumentException)
    }

    def "should be reusable across multiple values"() {
        given:
            def rule = Rule.when((int i) -> i % 2 == 0)
                           .orElse("Not even")

        expect:
            rule.check(2).isSuccess()
            rule.check(4).isSuccess()
            rule.check(5).isFailure()
            rule.check(7).isFailure()
    }

    def "should work for non-primitive input types"() {
        given:
            def rule = Rule.when((Dummy d) -> d.id > 0)
                           .orElse(d -> "Invalid ID: " + d.id)

        expect:
            rule.check(new Dummy(id: -1)).isFailure()
    }

    static class Dummy {
        int id
    }
}
