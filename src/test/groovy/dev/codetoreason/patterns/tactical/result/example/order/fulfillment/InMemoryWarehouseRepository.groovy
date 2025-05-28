package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

import dev.codetoreason.patterns.tactical.infra.repository.InMemoryEntityRepository

class InMemoryWarehouseRepository extends InMemoryEntityRepository<Warehouse, WarehouseId>
        implements WarehouseRepository {

    @Override
    List<Warehouse> findAllByRegion(String region) {
        findAllBy {
            it.region().equals(region)
        }
    }
}

