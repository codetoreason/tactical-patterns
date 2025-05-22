package dev.codetoreason.patterns.tactical.result.snippets

import dev.codetoreason.patterns.tactical.quantity.Quantity
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.OrderId
import dev.codetoreason.patterns.tactical.result.example.order.fulfillment.WarehouseId
import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.snippets.ProductType.STANDARD

class WarehouseSelectorSpec extends Specification {

    static final def DEFAULT_PRODUCT = Product.builder()
                                              .id(new ProductId("PRODUCT"))
                                              .name("PRODUCT")
                                              .type(STANDARD)
                                              .build()

    def warehouseRepoStub = Stub(WarehouseRepository)
    def warehouseSelector = new WarehouseSelector(warehouseRepoStub)

    def "should return success result when warehouse matches"() {
        given:
            def region = "REGION"
            def order = prepareOrder(region, Quantity.of(1))
            def warehouse = prepareWarehouse(region, Quantity.of(10))
        and:
            warehouseRepoStub.findByRegion("REGION") >> [warehouse]

        when:
            def result = warehouseSelector.selectFor(order)

        then:
            result.isSuccess()
            result.value().get() == warehouse
            result.message() == "OK"
    }

    def "should return failure result when no warehouse in region"() {
        given:
            def order = prepareOrder("EMPTY", Quantity.of(1))
        and:
            warehouseRepoStub.findByRegion("EMPTY") >> []

        when:
            def result = warehouseSelector.selectFor(order)

        then:
            result.isFailure()
            result.value().isEmpty()
            result.message() == "No warehouses available in region: EMPTY"
    }

    private static def prepareOrder(String destinationRegion, Quantity productQuantity) {
        Order.builder()
             .id(new OrderId("ORDER"))
             .destinationRegion(destinationRegion)
             .product(DEFAULT_PRODUCT)
             .quantity(productQuantity)
             .build()
    }

    private static def prepareWarehouse(String region, Quantity stockLevel) {
        Warehouse.builder()
                 .id(new WarehouseId("W1"))
                 .region(region)
                 .supportedTypes(Set.of(DEFAULT_PRODUCT.type()))
                 .stockLevels(Map.of(DEFAULT_PRODUCT.id(), stockLevel))
                 .build()
    }
}
