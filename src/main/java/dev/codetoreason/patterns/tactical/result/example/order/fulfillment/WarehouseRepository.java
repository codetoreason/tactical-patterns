package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

import java.util.List;

interface WarehouseRepository extends EntityRepository<Warehouse, WarehouseId> {

    List<Warehouse> findAllByRegion(String region);

}
