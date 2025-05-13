package dev.codetoreason.patterns.tactical.result.example.order.fulfillment;

import java.util.List;

interface WarehouseRepository {

    List<Warehouse> findByRegion(String region);

}
