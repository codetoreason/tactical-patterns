package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.infra.event.EventPublisher;

public class OrderFulfillmentFacadeFactory {

    OrderFulfillmentFacade create(
            ShippingService shippingService,
            EventPublisher eventPublisher,
            OrderRepository orderRepository,
            WarehouseRepository warehouseRepository
    ) {
        return new OrderFulfillmentFacade(
                shippingService,
                eventPublisher,
                orderRepository,
                new WarehouseSelector(
                        warehouseRepository
                )
        );
    }
}
