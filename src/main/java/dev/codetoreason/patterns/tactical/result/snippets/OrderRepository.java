package dev.codetoreason.patterns.tactical.result.snippets;

import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderId;

import java.util.Optional;

interface OrderRepository {

    Optional<Order> findById(OrderId orderId);

}
