package dev.codetoreason.patterns.tactical.result.example.order.fulfillment

class InMemoryWarehouseRepository implements WarehouseRepository {

    private final Map<String, List<Warehouse>> storage = new HashMap<>()

    @Override
    List<Warehouse> findByRegion(String region) {
        storage.getOrDefault(region, List.of())
    }

    void save(Warehouse warehouse) {
        storage.computeIfAbsent(warehouse.region(), r -> new ArrayList<>())
               .add(warehouse)
    }
}

