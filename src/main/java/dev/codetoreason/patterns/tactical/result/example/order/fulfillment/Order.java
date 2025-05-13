package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

record Order(
        OrderId id,
        Product product,
        int quantity,
        String destinationRegion
) {
}
