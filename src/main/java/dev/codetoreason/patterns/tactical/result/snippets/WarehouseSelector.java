package dev.codetoreason.patterns.tactical.result.snippets;

class WarehouseSelector {

    private final WarehouseRepository warehouseRepository;

    WarehouseSelector(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    Result<Warehouse> selectFor(Order order) {
        var destinationRegion = order.destinationRegion();
        var regionWarehouses = warehouseRepository.findByRegion(destinationRegion);
        if (regionWarehouses.isEmpty()) {
            return Result.failed("No warehouses available in region: " + destinationRegion);
        }
        var product = order.product();
        var productType = product.type();
        var capableWarehouses = regionWarehouses.stream()
                                                .filter(w -> w.supports(productType))
                                                .toList();
        if (capableWarehouses.isEmpty()) {
            return Result.failed("No warehouse capable of handling product type: " + productType);
        }
        var quantity = order.quantity();
        var warehouseWithStock = capableWarehouses.stream()
                                                  .filter(w -> w.hasProduct(product, quantity))
                                                  .findFirst();
        return warehouseWithStock.map(Result::successful)
                                 .orElseGet(() -> Result.failed(
                                         "Insufficient stock for product '%s' x%d in region: %s"
                                                 .formatted(product.name(), quantity.value(), destinationRegion)
                                 ));

    }
}

