package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import lombok.Builder;

@Builder
record Product(
        ProductId id,
        String name,
        ProductType type
) {
}
