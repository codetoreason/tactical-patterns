package dev.codetoreason.patterns.tactical.quantity;

/**
 * Represents a non-negative quantity of items, used to express stock levels, order sizes,
 * and other discrete measurable amounts in the system.
 * <p>
 * A {@code Quantity} is a value object that encapsulates an {@code int} value
 * and enforces domain constraints such as non-negativity.
 * <p>
 * Instances of this class are immutable and validated at creation time.
 *
 * <p>Usage examples:
 * <pre>{@code
 * Quantity five = Quantity.of(5);
 * Quantity zero = Quantity.zero();
 * boolean ok = warehouse.hasProduct(product, five);
 * }</pre>
 *
 * <p>Domain constraints:
 * <ul>
 *   <li>Must not be negative — creating a quantity with value &lt; 0 will throw {@link IllegalArgumentException}</li>
 *   <li>Quantity of zero is allowed and explicitly supported via {@link #isZero()}</li>
 * </ul>
 *
 * @param value the number of units (must be zero or positive)
 */
public record Quantity(int value) {

    /**
     * Creates a new quantity with the given value.
     *
     * @param value the number of units (must be ≥ 0)
     * @throws IllegalArgumentException if {@code value} is negative
     */
    public Quantity {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    /**
     * Creates a new {@code Quantity} instance from the given value.
     *
     * @param value number of units
     * @return a validated {@code Quantity}
     * @throws IllegalArgumentException if {@code value} is negative
     */
    public static Quantity of(int value) {
        return new Quantity(value);
    }

    /**
     * Returns a {@code Quantity} representing zero units.
     *
     * @return a quantity of zero
     */
    public static Quantity zero() {
        return new Quantity(0);
    }

    /**
     * Returns whether this quantity is exactly zero.
     *
     * @return {@code true} if the quantity is 0
     */
    public boolean isZero() {
        return value == 0;
    }

    /**
     * Returns whether this quantity is greater than zero.
     *
     * @return {@code true} if the quantity is positive
     */
    public boolean isPositive() {
        return value > 0;
    }
}

