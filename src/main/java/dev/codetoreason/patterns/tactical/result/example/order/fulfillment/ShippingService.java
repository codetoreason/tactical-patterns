package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

public interface ShippingService {

    ShipmentId dispatch(OrderId orderId, WarehouseId warehouseId);

}

