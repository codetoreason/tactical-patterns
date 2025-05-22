package dev.codetoreason.patterns.tactical.result.snippets;

import dev.codetoreason.patterns.tactical.infra.event.EventPublisher;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfilledEvent;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderId;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderRejectedEvent;
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ShippingService;

class OrderFulfillmentFacade {

    private final ShippingService shippingService;
    private final EventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final WarehouseSelector warehouseSelector;

    OrderFulfillmentFacade(
            ShippingService shippingService,
            EventPublisher eventPublisher,
            OrderRepository orderRepository,
            WarehouseSelector warehouseSelector
    ) {
        this.shippingService = shippingService;
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
        this.warehouseSelector = warehouseSelector;
    }

    public void attemptFulfillment(OrderId orderId) {
        var maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            eventPublisher.publish(
                    new OrderRejectedEvent(
                            orderId,
                            "Order with id %s not found".formatted(orderId.value())
                    )
            );
            return;
        }
        var order = maybeOrder.get();
        var selectionResult = warehouseSelector.selectFor(order);
        if (selectionResult.isFailure()) {
            eventPublisher.publish(
                    new OrderRejectedEvent(orderId, selectionResult.message())
            );
            return;
        }
        var warehouse = selectionResult.value()
                                       .orElseThrow(() -> new IllegalStateException(
                                               "Warehouse selection result is successful but has no value"
                                       ));
        var warehouseId = warehouse.id();
        var shipmentId = shippingService.dispatch(orderId, warehouseId);
        eventPublisher.publish(
                new OrderFulfilledEvent(orderId, warehouseId, shipmentId)
        );
    }
}

