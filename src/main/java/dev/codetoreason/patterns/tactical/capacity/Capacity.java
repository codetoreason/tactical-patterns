package dev.codetoreason.patterns.tactical.capacity;

import dev.codetoreason.patterns.tactical.quantity.Quantity;

/**
 * Represents a capacity constraint in terms of a non-negative quantity.
 * <p>
 * A {@code Capacity} wraps a {@link Quantity} to give it semantic meaning
 * as a limit, upper bound, or maximum allowable value for a domain concept.
 * This improves clarity and safety when modeling business rules.
 *
 * <p>Domain examples:
 * <ul>
 *   <li>Warehouse capacity — maximum number of items that can be stored</li>
 *   <li>Truck capacity — upper bound of allowed weight or volume</li>
 *   <li>Inventory capacity — threshold before restocking is blocked</li>
 * </ul>
 *
 * <p>This is a value object. Instances are immutable and validated on creation.
 *
 * <p>Usage example:
 * <pre>{@code
 * Capacity limit = Capacity.with(Quantity.of(100));
 * Quantity incoming = Quantity.of(120);
 * if (limit.isExceededBy(incoming)) {
 *     throw new CapacityExceededException();
 * }
 * }</pre>
 *
 * @param quantity the quantity representing the capacity limit (must be non-null)
 * @see Quantity
 */
public record Capacity(Quantity quantity) {

    /**
     * A constant representing zero capacity.
     * Equivalent to {@code Capacity.with(Quantity.ZERO)}.
     */
    public static final Capacity ZERO = new Capacity(Quantity.ZERO);

    /**
     * Constructs a new {@code Capacity} with the given quantity.
     *
     * @param quantity the capacity quantity (must not be null)
     * @throws IllegalArgumentException if {@code quantity} is null
     */
    public Capacity {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity must be non-null");
        }
    }

    /**
     * Creates a new {@code Capacity} instance from the given quantity.
     *
     * @param quantity the quantity representing the capacity
     * @return a validated {@code Capacity}
     * @throws IllegalArgumentException if {@code quantity} is null
     */
    public static Capacity with(Quantity quantity) {
        return new Capacity(quantity);
    }

    /**
     * Returns whether this capacity is exactly zero.
     *
     * @return {@code true} if the underlying quantity is zero
     */
    public boolean isZero() {
        return quantity.isZero();
    }

    /**
     * Returns whether the given quantity exceeds this capacity.
     *
     * @param quantity the quantity to compare
     * @return {@code true} if {@code quantity > this.quantity}
     */
    public boolean isExceededBy(Quantity quantity) {
        return quantity.isGreaterThan(this.quantity);
    }
}
