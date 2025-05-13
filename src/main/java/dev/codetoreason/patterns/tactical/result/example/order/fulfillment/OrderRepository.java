package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import java.util.Optional;

interface OrderRepository {

    Optional<Order> findById(OrderId orderId);

}
