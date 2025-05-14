package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.event.Event

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ProductType.STANDARD

class OrderFulfillmentFixture {

    static final OrderId ORDER_ID = new OrderId("ORDER-1")
    static final Product PRODUCT = Product.builder()
                                          .id(new ProductId("PROD-1"))
                                          .name("AnyName")
                                          .type(STANDARD)
                                          .build()
    static final ShipmentId SHIPMENT_ID = new ShipmentId("shipment-123")
    static final String DEFAULT_REGION_NAME = "REGION-1"

    private final def shippingService = new FakeShippingService()
    private final def eventPublisher = new CapturingEventPublisher()
    private final def orderRepository = new InMemoryOrderRepository()
    private final def warehouseRepository = new InMemoryWarehouseRepository()
    private final def factory = new OrderFulfillmentFacadeFactory()

    static OrderFulfillmentFixture create() {
        new OrderFulfillmentFixture()
    }

    static OrderFulfillmentFacade freshFacade() {
        new OrderFulfillmentFixture().buildFacade()
    }

    WarehouseBuilder withWarehouse(WarehouseId id) {
        new WarehouseBuilder(this, id)
    }

    OrderBuilder withOrder() {
        new OrderBuilder(this)
    }

    OrderFulfillmentFacade buildFacade() {
        factory.create(
                shippingService,
                eventPublisher,
                orderRepository,
                warehouseRepository
        )
    }

    void verifyEventPublished(Event event) {
        eventPublisher.verifyLastPublished(event)
    }

    class WarehouseBuilder {
        private final OrderFulfillmentFixture fixture
        private final WarehouseId id
        private String region = DEFAULT_REGION_NAME
        private Set<ProductType> supportedTypes = Set.of(PRODUCT.type())
        private int quantity = 10

        WarehouseBuilder(OrderFulfillmentFixture fixture, WarehouseId id) {
            this.fixture = fixture
            this.id = id
        }

        WarehouseBuilder assignedToRegion(String region) {
            this.region = region
            this
        }

        WarehouseBuilder supporting(ProductType... types) {
            this.supportedTypes = Set.of(types)
            this
        }

        WarehouseBuilder stockingQuantity(int quantity) {
            this.quantity = quantity
            this
        }

        OrderFulfillmentFixture and() {
            fixture.warehouseRepository.save(
                    Warehouse.builder()
                             .id(id)
                             .region(region)
                             .supportedTypes(supportedTypes)
                             .stockLevels(Map.of(PRODUCT.id(), quantity))
                             .build()
            )
            fixture
        }
    }

    class OrderBuilder {
        private final OrderFulfillmentFixture fixture
        private int quantity = 5
        private String region = DEFAULT_REGION_NAME

        OrderBuilder(OrderFulfillmentFixture fixture) {
            this.fixture = fixture
        }

        OrderBuilder ofQuantity(int quantity) {
            this.quantity = quantity
            this
        }

        OrderBuilder toRegion(String region) {
            this.region = region
            this
        }

        OrderFulfillmentFixture and() {
            fixture.orderRepository.save(
                    Order.builder()
                         .id(ORDER_ID)
                         .product(PRODUCT)
                         .quantity(quantity)
                         .destinationRegion(region)
                         .build()
            )
            fixture
        }
    }
}
