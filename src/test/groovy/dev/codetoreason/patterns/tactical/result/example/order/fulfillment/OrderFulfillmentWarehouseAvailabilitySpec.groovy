package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.ORDER_ID
import static dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderFulfillmentFixture.PRODUCT
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
                                                 .assignedToRegion("OTHER-REGION")
                                                 .and()

            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderRejectedEvent(
                            ORDER_ID,
                            "No warehouses available in region: $orderRegion"
                    )
            )
    }

    def "should reject order when no warehouse supports product type"() {
        given:
            def orderedProductType = PRODUCT.type()
            def fixture = OrderFulfillmentFixture.create()
                                                 .withOrder()
                                                 .and()
                                                 .withWarehouse(new WarehouseId("W2"))
                                                 .supporting(PERISHABLE)
                                                 .and()

            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(ORDER_ID)

        then:
            fixture.verifyEventPublished(
                    new OrderRejectedEvent(
                            ORDER_ID,
                            "No warehouse capable of handling product type: $orderedProductType"
                    )
            )
    }
}