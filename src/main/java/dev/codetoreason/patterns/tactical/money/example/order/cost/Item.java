package dev.codetoreason.patterns.tactical.money.example.order.cost;

import dev.codetoreason.patterns.tactical.money.Money;
import dev.codetoreason.patterns.tactical.money.Price;
import dev.codetoreason.patterns.tactical.quantity.Quantity;

record Item(
        Product product,
        Price price,
        Quantity quantity
) {

    Money calculateCost() {
        return price.multiply(quantity);
    }
}
