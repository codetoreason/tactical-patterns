package dev.codetoreason.patterns.tactical.money;

import java.math.BigDecimal;

import static dev.codetoreason.patterns.tactical.money.Currency.PLN;

/**
 * Utility class for conveniently creating {@link Money} instances in specific currencies.
 * <p>
 * Designed for use in tests or domain-specific factories where boilerplate creation
 * of {@code Money} objects with fixed currencies (like PLN) would be repetitive.
 * <p>
 * Example usage:
 * <pre>{@code
 * Money amount = MoneyFactory.pln("50000");
 * }</pre>
 *
 * This class is not meant to be extended or instantiated.
 * All methods are static and focus on readability and test ergonomics.
 */
public final class MoneyFactory {

    private MoneyFactory() {
        // Prevent instantiation
    }

    /**
     * Creates a new {@link Money} object in {@link Currency#PLN} from a {@link String} representation of amount.
     *
     * @param amount the amount in string format (e.g. "10000", "99.99")
     * @return a {@code Money} object with the given amount and PLN currency
     * @throws NumberFormatException if the string is not a valid representation of a {@link BigDecimal}
     */
    public static Money pln(String amount) {
        return new Money(new BigDecimal(amount), PLN);
    }

}
