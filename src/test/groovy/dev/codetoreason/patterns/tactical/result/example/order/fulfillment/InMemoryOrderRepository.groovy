package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.repository.InMemoryEntityRepository

class InMemoryOrderRepository extends InMemoryEntityRepository<Order, OrderId> implements OrderRepository {
}
