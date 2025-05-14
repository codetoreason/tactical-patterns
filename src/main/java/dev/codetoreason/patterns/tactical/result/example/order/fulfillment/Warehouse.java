package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
record Warehouse(
        WarehouseId id,
        String region,
        Set<ProductType> supportedTypes,
        Map<ProductId, Integer> stockLevels
) {

    boolean hasProduct(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity == 0) {
            return supports(product.type());
        }
        var stockLevel = stockLevels.getOrDefault(product.id(), 0);
        return stockLevel >= quantity;
    }

    boolean supports(ProductType type) {
        return supportedTypes.contains(type);
    }

}
