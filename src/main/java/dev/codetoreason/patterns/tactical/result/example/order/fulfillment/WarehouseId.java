package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

public record WarehouseId(String value) {

    public WarehouseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("WarehouseId cannot be null or blank");
        }
    }
}
