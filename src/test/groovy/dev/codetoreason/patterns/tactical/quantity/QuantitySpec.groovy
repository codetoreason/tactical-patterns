package dev.codetoreason.patterns.tactical.quantity

import spock.lang.Specification
import spock.lang.Unroll

class QuantitySpec extends Specification {

    def "should create quantity when value is non-negative"() {
        expect:
            Quantity.of(0).value() == 0
            Quantity.of(5).value() == 5
    }

    def "should throw when value is negative"() {
        when:
            Quantity.of(-1)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Quantity cannot be negative"
    }

    def "should expose constant ZERO"() {
        expect:
            Quantity.ZERO == Quantity.of(0)
            Quantity.ZERO.value() == 0
            Quantity.ZERO.isZero()
    }

    def "should consider two quantities with same value equal"() {
        expect:
            Quantity.of(7) == Quantity.of(7)
            Quantity.of(7).hashCode() == Quantity.of(7).hashCode()
    }

    def "should consider quantities with different values not equal"() {
        expect:
            Quantity.of(5) != Quantity.of(3)
            Quantity.of(5).hashCode() != Quantity.of(3).hashCode()
    }


    @Unroll
    def "should detect isZero correctly for #val"() {
        expect:
            Quantity.of(val).isZero() == expected

        where:
            val || expected
            0   || true
            1   || false
    }

    @Unroll
    def "should detect isPositive correctly for #val"() {
        expect:
            Quantity.of(val).isPositive() == expected

        where:
            val || expected
            0   || false
            1   || true
    }

    @Unroll("#a > #b == #expected")
    def "should correctly compare quantities using isGreaterThan"() {
        expect:
            Quantity.of(a).isGreaterThan(Quantity.of(b)) == expected

        where:
            a | b || expected
            5 | 3 || true
            3 | 5 || false
            5 | 5 || false
    }

    @Unroll("#a >= #b == #expected")
    def "should correctly compare quantities using isGreaterThanOrEqualTo"() {
        expect:
            Quantity.of(a).isGreaterThanOrEqualTo(Quantity.of(b)) == expected

        where:
            a | b || expected
            5 | 3 || true
            3 | 5 || false
            5 | 5 || true
    }

    @Unroll("#a < #b == #expected")
    def "should correctly compare quantities using isLessThan"() {
        expect:
            Quantity.of(a).isLessThan(Quantity.of(b)) == expected

        where:
            a | b || expected
            5 | 3 || false
            3 | 5 || true
            5 | 5 || false
    }

    @Unroll("#a <= #b == #expected")
    def "should correctly compare quantities using isLessThanOrEqualTo"() {
        expect:
            Quantity.of(a).isLessThanOrEqualTo(Quantity.of(b)) == expected

        where:
            a | b || expected
            5 | 3 || false
            3 | 5 || true
            5 | 5 || true
    }
}
