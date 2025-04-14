package dev.codetoreason.patterns.tactical.result

import spock.lang.Specification

class ResultSpec extends Specification {

    def "should create successful result with non-null value"() {
        when:
            def result = Result.successful("Hello")

        then:
            result.success
            !result.failure
            result.message() == "OK"
            result.value().isPresent()
            result.value().get() == "Hello"
    }

    def "should throw when creating successful result with null value"() {
        when:
            Result.successful(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Value cannot be null for successful result"
    }

    def "should create failed result with message"() {
        when:
            def result = Result.failed("Something went wrong")

        then:
            !result.success
            result.failure
            result.message() == "Something went wrong"
            result.value().isEmpty()
    }

    def "should create failed result with formatted message"() {
        when:
            def result = Result.failed("Error: %s at %s", "NullPointer", "line 42")

        then:
            !result.success
            result.failure
            result.message() == "Error: NullPointer at line 42"
            result.value().isEmpty()
    }

    def "should throw when creating failed result with null message"() {
        when:
            Result.failed(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should throw when creating failed result with blank message"() {
        when:
            Result.failed("   ")

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should throw when formatted message is blank"() {
        when:
            Result.failed("   %s", "   ")

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should consider two successful results with equal values as equal"() {
        given:
            def r1 = Result.successful("X")
            def r2 = Result.successful("X")

        expect:
            r1 == r2
            r1.hashCode() == r2.hashCode()
    }

    def "should consider two failed results with same message as equal"() {
        given:
            def r1 = Result.failed("Boom")
            def r2 = Result.failed("Boom")

        expect:
            r1 == r2
            r1.hashCode() == r2.hashCode()
    }

    def "should consider failed and successful results as not equal"() {
        given:
            def r1 = Result.successful("Hello")
            def r2 = Result.failed("Error")

        expect:
            r1 != r2
            r1.hashCode() != r2.hashCode()
    }

    def "should consider failed results with different messages as not equal"() {
        given:
            def r1 = Result.failed("X")
            def r2 = Result.failed("Y")

        expect:
            r1 != r2
            r1.hashCode() != r2.hashCode()
    }

    def "should consider successful results with different values as not equal"() {
        given:
            def r1 = Result.successful("A")
            def r2 = Result.successful("B")

        expect:
            r1 != r2
            r1.hashCode() != r2.hashCode()
    }

    def "should return proper toString for success result"() {
        given:
            def result = Result.successful("Data")

        expect:
            result.toString() == "Result{result=SUCCESS, value=Data, message='OK'}"
    }

    def "should return proper toString for failed result"() {
        given:
            def result = Result.failed("Validation failed")

        expect:
            result.toString() == "Result{result=FAILURE, value=null, message='Validation failed'}"
    }
}

