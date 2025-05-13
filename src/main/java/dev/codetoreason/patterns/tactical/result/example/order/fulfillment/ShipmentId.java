package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

public record ShipmentId(String value) {

    public ShipmentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ShipmentId cannot be null or blank");
        }
    }
}

