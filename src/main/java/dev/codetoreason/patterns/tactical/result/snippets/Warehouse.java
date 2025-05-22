package dev.codetoreason.patterns.tactical.result.snippets;

import dev.codetoreason.patterns.tactical.quantity.Quantity;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.WarehouseId;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

import static dev.codetoreason.patterns.tactical.quantity.Quantity.ZERO;

@Builder
record Warehouse(
        WarehouseId id,
        String region,
        Set<ProductType> supportedTypes,
        Map<ProductId, Quantity> stockLevels
) {

    boolean hasProduct(Product product, Quantity quantity) {
        if (quantity.isZero()) {
            return supports(product.type());
        }
        var stockLevel = stockLevels.getOrDefault(product.id(), ZERO);
        return stockLevel.isGreaterThanOrEqualTo(quantity);
    }

    boolean supports(ProductType type) {
        return supportedTypes.contains(type);
    }

}
