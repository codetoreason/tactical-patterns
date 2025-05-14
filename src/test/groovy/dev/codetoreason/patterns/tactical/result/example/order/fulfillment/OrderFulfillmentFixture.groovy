package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.event.Event
import dev.codetoreason.patterns.tactical.infra.event.EventPublisher
import spock.lang.Specification

class OrderFulfillmentFixture extends Specification {

    static final ShipmentId SHIPMENT_ID = new ShipmentId("shipment-123")

    private final def shippingServiceStub = Stub(ShippingService) {
        dispatch(_ as OrderId, _ as WarehouseId) >> SHIPMENT_ID
    }
    private final def eventPublisherMock = Mock(EventPublisher)
    private final def orderRepository = new InMemoryOrderRepository()
    private final def warehouseRepository = new InMemoryWarehouseRepository()
    private final def factory = new OrderFulfillmentFacadeFactory()

    static OrderFulfillmentFacade freshFacade() {
        new OrderFulfillmentFixture().buildFacade()
    }

    static OrderFulfillmentFixture create() {
        new OrderFulfillmentFixture()
    }

    OrderFulfillmentFixture withOrder(Order order) {
        orderRepository.save(order)
        this
    }

    OrderFulfillmentFixture withWarehouse(Warehouse warehouse) {
        warehouseRepository.save(warehouse)
        this
    }

    OrderFulfillmentFacade buildFacade() {
        factory.create(
                shippingServiceStub,
                eventPublisherMock,
                orderRepository,
                warehouseRepository
        )
    }

    void verifyEventPublished(Event event) {
        1 * eventPublisherMock.publish(event)
    }
}
