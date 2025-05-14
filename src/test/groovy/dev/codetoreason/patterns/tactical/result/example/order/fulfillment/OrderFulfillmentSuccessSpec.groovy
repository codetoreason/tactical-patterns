package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.ORDER_ID
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.SHIPMENT_ID
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ProductType.CONTROLLED_SUBSTANCE

class OrderFulfillmentSuccessSpec extends Specification {

    def "should fulfill order when one matching warehouse"() {
        given:
            def warehouseId = new WarehouseId("w1")
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(5)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(warehouseId)
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(10)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderFulfilledEvent(ORDER_ID, warehouseId, SHIPMENT_ID)
            )
    }

    def "should fulfill order from eligible warehouse when others are invalid"() {
        given:
            def correctId = new WarehouseId("w3")
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .toRegion("DE")
                                                 .and()
                                                 .withWarehouse(new WarehouseId("w1"))
                                                 .assignedToRegion("PL")
                                                 .supportingOrderedProduct()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("w2"))
                                                 .assignedToRegion("DE")
                                                 .supporting(CONTROLLED_SUBSTANCE)
                                                 .and()
                                                 .withWarehouse(correctId)
                                                 .supportingOrderedProduct()
                                                 .assignedToRegion("DE")
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderFulfilledEvent(ORDER_ID, correctId, SHIPMENT_ID)
            )
    }

    def "should choose first eligible warehouse when multiple match"() {
        given:
            def firstId = new WarehouseId("w1")
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(3)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(firstId)
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(5)
                                                 .and()
                                                 .withWarehouse(new WarehouseId("w2"))
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(10)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderFulfilledEvent(ORDER_ID, firstId, SHIPMENT_ID)
            )
    }

    def "should fulfill order when stock equals quantity"() {
        given:
            def warehouseId = new WarehouseId("w4")
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(7)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(warehouseId)
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(7)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderFulfilledEvent(ORDER_ID, warehouseId, SHIPMENT_ID)
            )
    }

    def "should fulfill order when stock is more than quantity"() {
        given:
            def warehouseId = new WarehouseId("w5")
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .ofQuantity(3)
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(warehouseId)
                                                 .inDefaultRegion()
                                                 .supportingOrderedProduct()
                                                 .stockingQuantity(99)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderFulfilledEvent(ORDER_ID, warehouseId, SHIPMENT_ID)
            )
    }
}
