package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.SHIPMENT_ID

class FakeShippingService implements ShippingService {

    @Override
    ShipmentId dispatch(OrderId orderId, WarehouseId warehouseId) {
        return SHIPMENT_ID
    }

}
