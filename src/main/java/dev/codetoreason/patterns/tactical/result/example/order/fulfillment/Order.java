package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.quantity.Quantity;
import lombok.Builder;

@Builder
record Order(
        OrderId id,
        Product product,
        Quantity quantity,
        String destinationRegion
) {
}
