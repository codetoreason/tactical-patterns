package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import spock.lang.Specification

class OrderFulfillmentOrderValidationSpec extends Specification {

    def "should reject order when order id does not exist"() {
        given:
            def nonExistingOrderId = new OrderId("MISSING-ORDER")
            def fixture = OrderFulfillmentFixture.create()
            def facade = fixture.buildFacade()

        when:
            facade.attemptFulfillment(nonExistingOrderId)

        then:
            fixture.verifyOrderRejectedEventPublished()
                   .withReason(
                           "Order with id ${ nonExistingOrderId.value() } not found"
                   )
    }
}