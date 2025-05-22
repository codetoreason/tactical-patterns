package dev.codetoreason.patterns.tactical.result.snippets;

import java.util.List;

interface WarehouseRepository {

    List<Warehouse> findByRegion(String region);

}
