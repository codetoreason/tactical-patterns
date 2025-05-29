package dev.codetoreason.patterns.tactical.money;

import java.math.BigDecimal;

/**
 * Provides exchange rates for converting from any source currency
 * to a single predefined target currency.
 */
@FunctionalInterface
public interface TargetCurrencyRateProvider {

    /**
     * Returns the conversion factor to convert from the given source currency
     * to the target currency.
     *
     * @param sourceCurrency the currency to convert from
     * @return the multiplication factor (e.g. 4.23 for USD → PLN)
     * @throws IllegalArgumentException if the currency is not supported
     */
    BigDecimal factorFrom(Currency sourceCurrency);
}
