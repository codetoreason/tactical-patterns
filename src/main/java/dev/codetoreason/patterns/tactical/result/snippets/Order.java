package dev.codetoreason.patterns.tactical.result.snippets;

import dev.codetoreason.patterns.tactical.quantity.Quantity;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderId;
import lombok.Builder;

@Builder
record Order(
        OrderId id,
        Product product,
        Quantity quantity,
        String destinationRegion
) {
}
