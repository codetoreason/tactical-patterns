package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.event.Event
import dev.codetoreason.patterns.tactical.infra.event.EventPublisher

class CapturingEventPublisher implements EventPublisher {

    private Event lastPublished

    @Override
    void publish(Event event) {
        lastPublished = event
    }

    void verifyLastPublished(Event expected) {
        assert lastPublished == expected
    }
}
