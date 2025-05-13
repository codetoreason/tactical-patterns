package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.infra.event.Event;

public record OrderFulfilledEvent(
        OrderId orderId,
        WarehouseId warehouseId,
        ShipmentId shipmentId
) implements Event {
}
