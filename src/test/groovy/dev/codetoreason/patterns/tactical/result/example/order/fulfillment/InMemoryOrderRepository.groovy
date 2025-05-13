package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> storage = new HashMap<>()

    @Override
    Optional<Order> findById(OrderId orderId) {
        return Optional.ofNullable(storage.get(orderId))
    }

    void save(Order order) {
        storage.put(order.id(), order)
    }
}
