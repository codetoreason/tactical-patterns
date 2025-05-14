package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

record ProductId(String value) {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be null or blank");
        }
    }
}
