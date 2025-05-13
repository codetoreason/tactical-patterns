package dev.codetoreason.patterns.tactical.infra.event;

/**
 * A generic interface for publishing domain events.
 * <p>
 * Allows the application layer (e.g., facades or use cases) to emit events
 * without coupling to the underlying event bus, messaging infrastructure, or framework.
 * <p>
 * Typical implementations may deliver events:
 * <ul>
 *     <li>in-memory to listeners,</li>
 *     <li>to external message brokers (e.g., Kafka, RabbitMQ),</li>
 *     <li>or simply collect them in tests for verification.</li>
 * </ul>
 *
 * @see Event
 */
@FunctionalInterface
public interface EventPublisher {

    /**
     * Publishes the given event to interested handlers or infrastructure.
     *
     * @param event the event to be published (must not be {@code null})
     * @throws IllegalArgumentException if the event is {@code null}
     */
    void publish(Event event);
}
