# Stage 2 Conversation (JM)

## User

Since we introducing VIP customer and critic for stage 2 can you tell me which method override customer?

## Assistant

In Stage 2, both `VIPCustomer` and `CriticCustomer` extend `Customer`. They each provide implementations of the two abstract methods in `Customer`, and each overrides one additional method for its special behaviour.

| Customer type | Method | What it does |
|---|---|---|
| **VIP** | `getTypeName()` | Returns `"VIP"`. |
| **VIP** | `getWaitingDecay()` | Returns **5**, so a VIP loses 5 satisfaction points per waiting turn. |
| **VIP** | `applyCustomerDiscount(int baseCents)` | Overrides the default full-price method to apply a **10% discount**. |
| **Critic** | `getTypeName()` | Returns `"Critic"`. |
| **Critic** | `getWaitingDecay()` | Returns **12**, so a Critic loses 12 satisfaction points per waiting turn. |
| **Critic** | `chooseFood(...)` | Overrides the normal food-choice method so the Critic **always chooses Pasta**. |

`getTypeName()` and `getWaitingDecay()` are **abstract** in `Customer`, so every concrete customer subclass must implement them. By contrast, `applyCustomerDiscount()` and `chooseFood()` already have default implementations in `Customer`; the subclasses override them only where they need different behaviour.

For example, `RegularCustomer` uses the default discount method, which returns the full price, and the default food-choice strategy.

## User

can you check, if the waiter and order need to be change?

## Assistant

Yes, **both `Waiter` and `Order` changed** between your Stage 1 and Stage 2 files. However, the reason for each change is different.

### `Waiter`: changed to apply the customer’s discount

In Stage 1, the Waiter creates an order using the menu item’s full price:

```java
new Order(customer, item, item.getPriceCents())
```

In Stage 2, the Waiter asks the customer object to calculate the price first:

```java
new Order(customer, item, customer.applyCustomerDiscount(item.getPriceCents()))
```

This change allows a VIP to apply the 10% discount. For a Regular or Critic customer, the inherited method returns the full price. The Waiter does not need to check the customer’s type.

### `Order`: renamed to make the locked price clearer

In Stage 2, `priceCents` was renamed to `lockedPriceCents`, and `getPriceCents()` was renamed to `getLockedPriceCents()`.

The important distinction is that **Stage 1 already stored the price in a `final` field**, so its price was already fixed when the order was created. The Stage 2 rename makes that purpose clearer; it was **not necessary to lock the price for the first time**. The existing Stage 1 field could also have stored the discounted amount passed by the Waiter.

Because the getter was renamed, callers such as `Cashier`, `GameMgr` and `RestaurantUI` also needed to use the new getter name.
