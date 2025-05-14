package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import lombok.Builder;

@Builder
record Order(
        OrderId id,
        Product product,
        int quantity,
        String destinationRegion
) {
}
