# tactical-patterns

Tactical, business-oriented patterns that simplify code, express intent, and support testability and architectural flexibility.

![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)
![Java 24](https://img.shields.io/badge/Java-24-blue)

---

## ✨ Purpose

This repository contains small but powerful *tactical patterns* designed to:

* clarify domain intent,
* reduce accidental complexity,
* and improve testability of business rules and services.

Each pattern is self-contained, immutable, and aligned with **Business-Oriented Programming** and **Test-Driven Culture**.

---

## 📦 Patterns included

### 💰 `Money`

Value Object for currency-aware amounts. Enforces arithmetic constraints and eliminates primitives like `BigDecimal` scattered across code.

```java
Money amount = Money.of(new BigDecimal("1000.00"), Currency.PLN);
if (amount.isPositive()) { ... }
```

### 🔢 `Quantity`

A non-negative VO for discrete quantities (e.g. items, units). Encapsulates validation, equality, and comparison logic.

```java
Quantity q = Quantity.of(5);
if (q.isGreaterThan(Quantity.ZERO)) { ... }
```

### ✅ `OperationResult`

Minimal object for signaling success/failure with a message. An alternative to `boolean` or exceptions for low-level business rules.

```java
OperationResult result = validator.validate(applicant);
if (result.isFailure()) return result.message();
```

### 🏱 `Result<T>`

Extended version of `OperationResult` that includes a return value on success. Inspired by `Optional<T>` and `Either`, but focused on clarity and simplicity.

```java
Result<User> result = userService.findById(id);
if (result.isSuccess()) return result.value().get();
```

---

## 💪 Test Patterns & DSLs

Patterns in this repo come with carefully designed **test DSLs** and **fixtures** that emphasize:

* test clarity over technical plumbing,
* fluent builders for arranging test scenarios,
* and readable assertions based on business behavior.

Example:

```groovy
withWarehouse(new WarehouseId("WH-1"))
    .assignedToRegion("Mazovia")
    .supportingOrderedProduct()
    .stockingQuantity(10)
    .and()

withOrder()
    .ofQuantity(5)
    .toRegion("Mazovia")
    .and()
```

---

## 🤝 License

MIT — use freely in your tactical stack.
