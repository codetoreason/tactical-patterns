package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.ORDER_ID
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.PRODUCT
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ProductType.CONTROLLED_SUBSTANCE
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.ProductType.PERISHABLE

class OrderFulfillmentWarehouseAvailabilitySpec extends Specification {

    def "should reject order when no warehouses in order's region"() {
        given:
            def orderRegion = "X-NON-EXISTENT"
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .toRegion(orderRegion)
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W1"))
                                                 .inDefaultRegion()
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyOrderRejectedEventPublished()
                   .withReason(
                           "No warehouses available in region: $orderRegion"
                   )
    }

    def "should reject order when no warehouse supports product type"() {
        given:
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .toDefaultRegion()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W2"))
                                                 .inDefaultRegion()
                                                 .supporting(PERISHABLE, CONTROLLED_SUBSTANCE)
                                                 .and()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyOrderRejectedEventPublished()
                   .withReason(
                           "No warehouse capable of handling product type: ${ PRODUCT.type() }"
                   )
    }
}