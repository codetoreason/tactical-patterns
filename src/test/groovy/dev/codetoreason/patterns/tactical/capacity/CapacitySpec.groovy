package dev.codetoreason.patterns.tactical.capacity

import dev.codetoreason.patterns.tactical.quantity.Quantity
import spock.lang.Specification
import spock.lang.Unroll

class CapacitySpec extends Specification {

    def "should create Capacity using constructor with valid Quantity"() {
        given:
            def quantity = Quantity.of(10)

        when:
            def capacity = new Capacity(quantity)

        then:
            capacity != null
    }

    def "should throw when Quantity is null"() {
        when:
            new Capacity(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Quantity must be non-null"
    }

    def "should report zero for Capacity.ZERO"() {
        expect:
            Capacity.ZERO.isZero()
    }

    def "should return false from isZero() for capacity > 0"() {
        given:
            def capacity = Capacity.with(Quantity.of(5))

        expect:
            !capacity.isZero()
    }

    def "should create capacity via factory method with(...)"() {
        given:
            def quantity = Quantity.of(20)

        when:
            def capacity1 = new Capacity(quantity)
            def capacity2 = Capacity.with(quantity)

        then:
            capacity1 == capacity2
    }

    @Unroll("#dataVariables")
    def "should correctly evaluate isExceededBy"() {
        given:
            def capacity = Capacity.with(Quantity.of(limit))

        expect:
            capacity.isExceededBy(Quantity.of(incoming)) == expected

        where:
            limit | incoming || expected
            10    | 12       || true
            10    | 10       || false
            10    | 8        || false
    }

    def "should evaluate isExceededBy against ZERO capacity"() {
        expect:
            Capacity.ZERO.isExceededBy(Quantity.of(1))
            !Capacity.with(Quantity.of(1)).isExceededBy(Quantity.ZERO)
            !Capacity.ZERO.isExceededBy(Quantity.ZERO)
    }

    def "should handle large quantities in isExceededBy"() {
        given:
            def capacity = Capacity.with(Quantity.of(Integer.MAX_VALUE - 1))

        expect:
            capacity.isExceededBy(Quantity.of(Integer.MAX_VALUE))
    }

    def "should not exceed when incoming == capacity"() {
        given:
            def capacity = Capacity.with(Quantity.of(10))
            def incoming = Quantity.of(10)

        expect:
            !capacity.isExceededBy(incoming)
    }

    def "should consider capacities with same quantity equal"() {
        given:
            def quantity = Quantity.of(5)

        expect:
            Capacity.with(quantity) == Capacity.with(quantity)
            Capacity.with(quantity).hashCode() == Capacity.with(quantity).hashCode()
    }

    def "should consider capacities with different quantities not equal"() {
        def a = Capacity.with(Quantity.of(5))
        def b = Capacity.with(Quantity.of(10))

        expect:
            a != b
            a.hashCode() != b.hashCode()
    }

    def "should return correct string representation"() {
        given:
            def capacity = Capacity.with(Quantity.of(42))

        expect:
            capacity.toString() == "Capacity[quantity=Quantity[value=42]]"
    }
}
