package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

interface OrderRepository extends EntityRepository<Order, OrderId> {
}
