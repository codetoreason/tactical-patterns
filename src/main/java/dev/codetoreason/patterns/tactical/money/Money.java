package dev.codetoreason.patterns.tactical.money;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * Value Object representing a monetary amount with a specific currency.
 * <p>
 * This class enforces:
 * <ul>
 *     <li>Immutable and type-safe representation of money</li>
 *     <li>Semantic clarity by combining amount and currency</li>
 *     <li>Domain constraints like currency-aware arithmetic</li>
 * </ul>
 * <p>
 * Commonly used in financial and ecommerce domains.
 *
 * @param amount   the monetary value (must be non-null)
 * @param currency the currency (e.g. PLN, EUR)
 */
public record Money(BigDecimal amount, Currency currency) {

    /**
     * Constructs a {@code Money} instance with the given amount and currency.
     * <p>
     * Validates that neither {@code amount} nor {@code currency} is null,
     * and normalizes the amount by stripping trailing zeros for consistent equality and hashing.
     *
     * @throws IllegalArgumentException if {@code amount} or {@code currency} is {@code null}
     */
    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        amount = amount.stripTrailingZeros();
    }


    /**
     * Returns a zero value of money in the given currency.
     *
     * @param currency the currency (must not be null)
     * @return a Money object with value 0 and the given currency
     */
    public static Money zero(Currency currency) {
        return new Money(ZERO, currency);
    }

    /**
     * Returns a monetary value with the given amount and currency.
     *
     * @param amount   the monetary value (must not be null)
     * @param currency the currency (must not be null)
     * @return a new Money instance
     */
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    /**
     * Adds two monetary values of the same currency.
     *
     * @param other the other money value
     * @return a new Money representing the sum
     * @throws IllegalArgumentException if the currencies don't match
     */
    public Money add(Money other) {
        if (currency != other.currency) {
            throw new IllegalArgumentException("Cannot add different currencies: " + currency + " and " + other.currency);
        }
        return new Money(amount.add(other.amount), currency);
    }

    /**
     * Subtracts the given monetary value from this one.
     * <p>
     * Both amounts must be in the same currency.
     *
     * @param other the money value to subtract (must have the same currency)
     * @return a new {@code Money} instance representing the result of the subtraction
     * @throws IllegalArgumentException if the currencies do not match
     */
    public Money subtract(Money other) {
        if (currency != other.currency) {
            throw new IllegalArgumentException("Cannot subtract different currencies: " + currency + " and " + other.currency);
        }
        return new Money(amount.subtract(other.amount), currency);
    }


    /**
     * Checks if this amount is zero.
     *
     * @return {@code true} if amount is 0
     */
    public boolean isZero() {
        return ZERO.compareTo(amount) == 0;
    }

    /**
     * Checks if this amount is strictly positive (greater than 0).
     *
     * @return {@code true} if amount > 0
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if this amount is negative.
     *
     * @return {@code true} if amount is less than 0
     */
    public boolean isNegative() {
        return amount.compareTo(ZERO) < 0;
    }

    /**
     * Returns whether this monetary amount is denominated in the given currency.
     * <p>
     * This method compares currencies by reference identity and throws if the provided currency is {@code null}.
     *
     * @param currency the currency to compare against (must not be {@code null})
     * @return {@code true} if the currencies match, {@code false} otherwise
     * @throws IllegalArgumentException if {@code currency} is {@code null}
     */
    public boolean matchesCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        return this.currency == currency;
    }

    /**
     * Converts this monetary amount to the given target currency using the specified exchange rate provider.
     * <p>
     * This method performs a currency conversion using a conversion factor obtained from the given provider.
     * If the target currency is the same as this money's currency, the method returns {@code this}.
     * Otherwise, it applies the conversion factor and returns a new {@code Money} instance in the target currency.
     *
     * @param targetCurrency the currency to convert to (must not be {@code null})
     * @param rateProvider   the exchange rate provider that supplies conversion factors (must not be {@code null})
     * @return a new {@code Money} instance in the {@code targetCurrency}
     * @throws IllegalArgumentException if {@code targetCurrency} or {@code rateProvider} is {@code null},
     *                                  or if the provider does not supply a conversion factor for the current currency
     */
    public Money convertTo(Currency targetCurrency, TargetCurrencyRateProvider rateProvider) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("Target currency must not be null");
        }
        if (rateProvider == null) {
            throw new IllegalArgumentException("Rate provider must not be null");
        }
        if (currency == targetCurrency) {
            return this;
        }
        var factor = rateProvider.factorFrom(currency);
        if (factor == null) {
            throw new IllegalArgumentException("Missing exchange rate from " + currency + " to " + targetCurrency);
        }
        return Money.of(
                amount.multiply(factor),
                targetCurrency
        );
    }
}

