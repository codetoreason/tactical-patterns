package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.infra.event.Event;

public record OrderRejectedEvent(
        OrderId orderId,
        String reason
) implements Event {
}
