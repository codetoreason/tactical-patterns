package dev.codetoreason.patterns.tactical.result.snippets;

import lombok.Builder;

@Builder
record Product(
        ProductId id,
        String name,
        ProductType type
) {
}
