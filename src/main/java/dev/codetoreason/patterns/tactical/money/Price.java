package dev.codetoreason.patterns.tactical.money;

import dev.codetoreason.patterns.tactical.quantity.Quantity;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * Value Object representing a non-negative price with a specific currency.
 * <p>
 * Enforces:
 * <ul>
 *     <li>Non-negative monetary value</li>
 *     <li>Immutable and currency-aware representation</li>
 *     <li>Safe arithmetic for business use (e.g., pricing, billing)</li>
 * </ul>
 * <p>
 * Suitable for representing catalog prices, unit costs, and base rates.
 *
 * @param amount   the price amount (must be ≥ 0 and non-null)
 * @param currency the currency (must be non-null)
 */
public record Price(BigDecimal amount, Currency currency) {

    /**
     * Constructs a {@code Price} instance with the given amount and currency.
     * Validates that amount is non-null, non-negative, and normalizes it
     * by stripping trailing zeros for consistent equality and hashing.
     *
     * @throws IllegalArgumentException if amount is null, negative, or currency is null
     */
    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Price must not be negative");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        amount = amount.stripTrailingZeros();
    }

    /**
     * Creates a zero price in the given currency.
     *
     * @param currency the currency (must not be null)
     * @return a Price object with value 0 and the given currency
     */
    public static Price zero(Currency currency) {
        return new Price(ZERO, currency);
    }

    /**
     * Creates a price from the given amount and currency.
     *
     * @param amount   the monetary value (must be ≥ 0)
     * @param currency the currency (must not be null)
     * @return a new Price instance
     */
    public static Price of(BigDecimal amount, Currency currency) {
        return new Price(amount, currency);
    }

    /**
     * Returns whether this price is zero.
     *
     * @return true if the price is 0
     */
    public boolean isZero() {
        return amount.compareTo(ZERO) == 0;
    }

    /**
     * Returns whether this price is strictly positive.
     *
     * @return true if the price > 0
     */
    public boolean isPositive() {
        return amount.compareTo(ZERO) > 0;
    }

    /**
     * Multiplies this price by the given quantity, returning the total cost as {@link Money}.
     * <p>
     * This method is useful in purchasing, invoicing, and cart calculations
     * where a single-unit price must be applied to multiple units.
     *
     * @param quantity the quantity of items or units (must not be null and must be ≥ 0)
     * @return a {@code Money} object representing the total value
     * @throws IllegalArgumentException if {@code quantity} is null or negative
     */
    public Money multiply(Quantity quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity must not be null");
        }
        return Money.of(
                amount.multiply(
                        BigDecimal.valueOf(quantity.value())
                ),
                currency
        );
    }
}
