package dev.codetoreason.patterns.tactical.money.example.order.cost;

import dev.codetoreason.patterns.tactical.money.Currency;
import dev.codetoreason.patterns.tactical.money.Money;
import dev.codetoreason.patterns.tactical.money.TargetCurrencyRateProvider;

import java.util.Collection;

record Order(Collection<Item> items) {

    Money calculateCost(Currency currency) {
        return items.stream()
                    .map(Item::calculateCost)
                    .filter(money -> money.matchesCurrency(currency))
                    .reduce(Money.zero(currency), Money::add);
    }

    Money calculateCost(Currency currency, TargetCurrencyRateProvider rateProvider) {
        return items.stream()
                    .map(Item::calculateCost)
                    .map(money -> money.convertTo(currency, rateProvider))
                    .reduce(Money.zero(currency), Money::add);
    }
}
