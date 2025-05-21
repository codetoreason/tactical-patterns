package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.DEFAULT_REGION_NAME
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.ORDER_ID
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.PRODUCT

class OrderFulfillmentStockValidationSpec extends Specification {

    def "should reject order when warehouse has zero stock"() {
        given:
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(1)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W4"))
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(0)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderRejectedEvent(
                            ORDER_ID,
                            "Insufficient stock for product '${ PRODUCT.name() }' x1 in region: $DEFAULT_REGION_NAME"
                    )
            )
    }

    def "should reject order when warehouse has less stock than required"() {
        given:
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(10)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W1"))
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(5)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderRejectedEvent(
                            ORDER_ID,
                            "Insufficient stock for product '${ PRODUCT.name() }' x10 in region: $DEFAULT_REGION_NAME"
                    )
            )
    }

    def "should reject order when multiple warehouses are short by one unit"() {
        given:
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(6)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W2"))
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(5)
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W3"))
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(5)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderRejectedEvent(
                            ORDER_ID,
                            "Insufficient stock for product '${ PRODUCT.name() }' x6 in region: $DEFAULT_REGION_NAME"
                    )
            )
    }
}