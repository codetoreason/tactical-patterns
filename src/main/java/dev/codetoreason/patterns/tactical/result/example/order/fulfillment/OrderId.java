package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

public record OrderId(String value) {

    public OrderId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderId cannot be null or blank");
        }
    }
}

