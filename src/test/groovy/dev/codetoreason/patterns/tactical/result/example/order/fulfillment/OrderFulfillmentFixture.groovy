package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.event.Event
import dev.codetoreason.patterns.tactical.quantity.Quantity

import static dev.codetoreason.patterns.tactical.quantity.Quantity.ZERO
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ProductType.STANDARD

/**
 * Fixture for testing {@link OrderFulfillmentFacade} scenarios with a clean, consistent DSL.
 *
 * This fixture uses a fixed {@code ORDER_ID} and {@code PRODUCT} across all tests.
 * The goal is to decouple test intent from redundant data setup, and focus on variations
 * in warehouse configuration and order quantity.
 *
 * Why this design?
 * - All tests evaluate whether the system can successfully fulfill a specific order
 *   based on warehouse availability and capabilities.
 * - The actual order data (ID, product type, name, etc.) is not under test — only fulfillment behavior is.
 * - By standardizing the order, tests can:
 *     • express intent more clearly through fluent builders,
 *     • reduce setup noise and duplication,
 *     • highlight the conditions under which fulfillment succeeds or fails.
 *
 * You can override default values if needed (e.g. for cross-product tests), but by default:
 * - {@code PRODUCT} has type {@code STANDARD} and ID {@code PROD-1}
 * - {@code ORDER_ID} is {@code ORDER-1}
 * - {@code SHIPMENT_ID} is fixed for test determinism
 *
 * This fixture is designed for readability, reuse, and expressiveness — not maximal flexibility.
 */

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
        private String region
        private Set<ProductType> supportedTypes = Set.of()
        private Quantity quantity = ZERO

        WarehouseBuilder(OrderFulfillmentFixture fixture, WarehouseId id) {
            this.fixture = fixture
            this.id = id
        }

        WarehouseBuilder inDefaultRegion() {
            assignedToRegion(DEFAULT_REGION_NAME)
        }

        WarehouseBuilder assignedToRegion(String region) {
            this.region = region
            this
        }

        WarehouseBuilder supportingOrderedProduct() {
            supporting(PRODUCT.type())
        }


        WarehouseBuilder supporting(ProductType... types) {
            this.supportedTypes = Set.of(types)
            this
        }

        WarehouseBuilder stockingQuantity(int quantity) {
            this.quantity = Quantity.of(quantity)
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
        private Quantity quantity = ZERO
        private String region

        OrderBuilder(OrderFulfillmentFixture fixture) {
            this.fixture = fixture
        }

        OrderBuilder ofQuantity(int quantity) {
            this.quantity = Quantity.of(quantity)
            this
        }

        OrderBuilder toDefaultRegion() {
            toRegion(DEFAULT_REGION_NAME)
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
