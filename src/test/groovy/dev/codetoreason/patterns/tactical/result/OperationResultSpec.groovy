package dev.codetoreason.patterns.tactical.result

import spock.lang.Specification

class OperationResultSpec extends Specification {

    def "should return success result"() {
        when:
            def result = OperationResult.successful()

        then:
            result.success
            !result.failure
            result.message() == "OK"
    }

    def "should return failure result with message"() {
        when:
            def result = OperationResult.failed("Something went wrong")

        then:
            !result.success
            result.failure
            result.message() == "Something went wrong"
    }

    def "should return failure result with formatted message"() {
        when:
            def result = OperationResult.failed("Error: %s at %s", "NullPointer", "line 42")

        then:
            !result.success
            result.failure
            result.message() == "Error: NullPointer at line 42"
    }

    def "should throw when creating failure with null message"() {
        when:
            OperationResult.failed(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should throw when creating failure with blank message"() {
        when:
            OperationResult.failed("   ")

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should throw when formatted message is blank"() {
        when:
            OperationResult.failed("   %s", "   ")

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Message cannot be null or empty"
    }

    def "should consider two successful results as equal"() {
        given:
            def result1 = OperationResult.successful()
            def result2 = OperationResult.successful()

        expect:
            result1 == result2
            result1.hashCode() == result2.hashCode()
    }

    def "should treat two failed results with the same message as equal"() {
        given:
            def result1 = OperationResult.failed("Error 1")
            def result2 = OperationResult.failed("Error 1")

        expect:
            result1 == result2
            result1.hashCode() == result2.hashCode()
    }

    def "should consider results with different failure messages as not equal"() {
        given:
            def result1 = OperationResult.failed("Error 1")
            def result2 = OperationResult.failed("Error 2")

        expect:
            result1 != result2
            result1.hashCode() != result2.hashCode()
    }

    def "should consider failed and successful results as not equal"() {
        given:
            def result1 = OperationResult.successful()
            def result2 = OperationResult.failed("Error")

        expect:
            result1 != result2
            result1.hashCode() != result2.hashCode()
    }

    def "should return proper toString for success"() {
        given:
            def result = OperationResult.successful()

        expect:
            result.toString() == "OperationResult{isSuccess=true, message='OK'}"
    }

    def "should return proper toString for failure"() {
        given:
            def result = OperationResult.failed("Invalid state")

        expect:
            result.toString() == "OperationResult{isSuccess=false, message='Invalid state'}"
    }
}
