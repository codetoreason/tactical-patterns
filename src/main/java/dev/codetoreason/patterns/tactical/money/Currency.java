package dev.codetoreason.patterns.tactical.money;

/**
 * Enum representing supported currencies for monetary operations.
 * <p>
 * This enum is used in conjunction with the {@link Money} value object
 * to enforce type-safe and semantically meaningful currency handling.
 * <p>
 * Extend this enum as needed when supporting additional currencies.
 */
public enum Currency {
    USD, EUR, PLN
}
