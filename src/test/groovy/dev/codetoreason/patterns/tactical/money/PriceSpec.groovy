package dev.codetoreason.patterns.tactical.money

import dev.codetoreason.patterns.tactical.quantity.Quantity
import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.money.Currency.EUR
import static dev.codetoreason.patterns.tactical.money.Currency.USD
import static java.math.BigDecimal.TEN
import static java.math.BigDecimal.ZERO

class PriceSpec extends Specification {

    def "should create price with positive amount"() {
        when:
            def price = Price.of(new BigDecimal("10.00"), USD)

        then:
            price.amount() == new BigDecimal("10")
    }

    def "should create zero price with given currency"() {
        when:
            def price = Price.zero(USD)

        then:
            price.amount() == ZERO
            price.currency() == USD
    }

    def "should throw when creating price with negative amount"() {
        when:
            Price.of(new BigDecimal("-99.99"), USD)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Price must not be negative"
    }

    def "should throw when creating price with null amount"() {
        when:
            Price.of(null, USD)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Amount must not be null"
    }

    def "should throw when creating price with null currency"() {
        when:
            Price.of(TEN, null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Currency must not be null"
    }

    def "should throw when creating zero price with null currency"() {
        when:
            Price.zero(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Currency must not be null"
    }

    def "should detect zero price"() {
        expect:
            Price.zero(USD).isZero()
            Price.of(ZERO, USD).isZero()
    }

    def "should correctly identify positive price values"() {
        expect:
            Price.of(new BigDecimal("5.00"), USD).isPositive()
            !Price.of(new BigDecimal("0.00"), USD).isPositive()
    }

    def "should consider two price instances equal if value and currency match"() {
        expect:
            Price.of(new BigDecimal("10.00"), USD) == Price.of(new BigDecimal("10.00"), USD)
    }

    def "should not consider price equal if value differs"() {
        expect:
            Price.of(new BigDecimal("10.00"), USD) != Price.of(new BigDecimal("9.99"), USD)
    }

    def "should not consider price equal if currency differs"() {
        expect:
            Price.of(new BigDecimal("10.00"), USD) != Price.of(new BigDecimal("10.00"), EUR)
    }

    def "should consider price equal despite different BigDecimal scale"() {
        expect:
            Price.of(new BigDecimal("10.0"), USD) == Price.of(new BigDecimal("10.00"), USD)
    }

    def "should consider price values equal despite BigDecimal scale difference"() {
        given:
            def priceA = Price.of(new BigDecimal("10.0"), USD)
            def priceB = Price.of(new BigDecimal("10.00"), USD)

        expect:
            priceA == priceB
            priceA.hashCode() == priceB.hashCode()
    }

    def "multiply returns correct Money value for positive quantity"() {
        given:
            def price = Price.of(new BigDecimal("19.99"), EUR)
            def quantity = new Quantity(3)

        when:
            def total = price.multiply(quantity)

        then:
            total.amount() == new BigDecimal("59.97")
            total.currency() == EUR
    }

    def "multiply returns zero Money for zero quantity"() {
        given:
            def price = Price.of(new BigDecimal("19.99"), EUR)
            def quantity = new Quantity(0)

        when:
            def total = price.multiply(quantity)

        then:
            total.amount() == ZERO
            total.currency() == EUR
    }

    def "multiply throws when quantity is null"() {
        given:
            def price = Price.of(TEN, EUR)

        when:
            price.multiply(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Quantity must not be null"
    }
}
