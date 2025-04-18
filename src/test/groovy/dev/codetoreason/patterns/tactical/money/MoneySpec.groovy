package dev.codetoreason.patterns.tactical.money

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.money.Currency.EUR
import static dev.codetoreason.patterns.tactical.money.Currency.USD

class MoneySpec extends Specification {

    def "should create money with positive amount"() {
        when:
            def money = Money.of(new BigDecimal("10.00"), USD)

        then:
            money.amount() == new BigDecimal("10")
    }

    def "should create zero money with given currency"() {
        when:
            def money = Money.zero(USD)

        then:
            money.amount() == BigDecimal.ZERO
            money.currency() == USD
    }

    def "should create money with negative amount"() {
        when:
            def money = Money.of(new BigDecimal("-99.99"), USD)

        then:
            money.amount() == new BigDecimal("-99.99")
            money.currency() == USD
    }

    def "should throw when creating money with null amount"() {
        when:
            Money.of(null, USD)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Amount must not be null"
    }

    def "should throw when creating money with null currency"() {
        when:
            Money.of(BigDecimal.TEN, null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Currency must not be null"
    }

    def "should throw when creating zero money with null currency"() {
        when:
            Money.zero(null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Currency must not be null"
    }

    def "should add two money values with same currency"() {
        given:
            def a = Money.of(new BigDecimal("10.00"), USD)
            def b = Money.of(new BigDecimal("15.50"), USD)

        when:
            def result = a.add(b)

        then:
            result.amount() == new BigDecimal("25.50")
            result.currency() == USD
    }

    def "should throw when adding money with different currency"() {
        given:
            def a = Money.of(BigDecimal.TEN, USD)
            def b = Money.of(BigDecimal.TEN, EUR)

        when:
            a.add(b)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Cannot add different currencies: USD and EUR"
    }

    def "should subtract two money values with same currency"() {
        given:
            def a = Money.of(new BigDecimal("20.00"), USD)
            def b = Money.of(new BigDecimal("5.00"), USD)

        when:
            def result = a.subtract(b)

        then:
            result.amount() == new BigDecimal("15.00")
            result.currency() == USD
    }

    def "should throw when subtracting money with different currency"() {
        given:
            def a = Money.of(BigDecimal.TEN, USD)
            def b = Money.of(BigDecimal.ONE, EUR)

        when:
            a.subtract(b)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "Cannot subtract different currencies: USD and EUR"
    }

    def "should detect zero money"() {
        expect:
            Money.zero(USD).isZero()
    }

    def "should correctly identify positive money values"() {
        expect:
            Money.of(new BigDecimal("5.00"), USD).isPositive()
            !Money.of(new BigDecimal("0.00"), USD).isPositive()
            !Money.of(new BigDecimal("-3.00"), USD).isPositive()
    }

    def "should correctly identify negative money values"() {
        expect:
            !Money.of(new BigDecimal("5.00"), USD).isNegative()
            !Money.of(new BigDecimal("0.00"), USD).isNegative()
            Money.of(new BigDecimal("-3.00"), USD).isNegative()
    }


    def "should consider two money instances equal if value and currency match"() {
        expect:
            Money.of(new BigDecimal("10.00"), USD) == Money.of(new BigDecimal("10.00"), USD)
    }

    def "should not consider money equal if value differs"() {
        expect:
            Money.of(new BigDecimal("10.00"), USD) != Money.of(new BigDecimal("9.99"), USD)
    }

    def "should not consider money equal if currency differs"() {
        expect:
            Money.of(new BigDecimal("10.00"), USD) != Money.of(new BigDecimal("10.00"), EUR)
    }

    def "should consider money equal despite different BigDecimal scale"() {
        expect:
            Money.of(new BigDecimal("10.0"), USD) == Money.of(new BigDecimal("10.00"), USD)
    }

    def "should consider money values equal despite BigDecimal scale difference"() {
        given:
            def moneyA = Money.of(new BigDecimal("10.0"), USD)
            def moneyB = Money.of(new BigDecimal("10.00"), USD)

        expect:
            moneyA == moneyB
            moneyA.hashCode() == moneyB.hashCode()
    }
}
