package dev.codetoreason.patterns.tactical.infra.event;

/**
 * Marker interface representing a domain-level event.
 * <p>
 * Events are used to signal that something significant has happened within the system,
 * often as a result of executing a business operation or use case.
 * <p>
 * This interface allows for type safety and consistency in event handling and publication mechanisms.
 * <p>
 * Events implementing this interface should be treated as immutable.
 *
 * @see EventPublisher
 */
public interface Event {
}
