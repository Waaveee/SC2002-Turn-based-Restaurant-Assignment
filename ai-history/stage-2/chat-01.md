## Question
In Stage 2 the VIPCustomer gets a 10% discount on their bill, so why is better for Waiter to compute and lock the discounted price inside Order at the moment the order is taken rather than having Cashier compute the discount dynamically at Step 4 payment?


## Reply
Computing and locking the discounted price inside `Order` when `Waiter` takes the order is superior object-oriented design across five core principles:

* **Single Responsibility Principle (SRP) and High Cohesion:** Under SRP, a class should have only one reason to change so that unrelated axes of change do not become coupled. In Step 4, `Cashier` has a single, cohesive responsibility: settle payment for the oldest `READY_TO_PAY` customer, add the bill to revenue, mark the customer and order `PAID`, and free the table. `Order` is the domain entity responsible for tracking a specific transaction's dish, preparation units, and agreed bill. If `Cashier` calculated discounts at Step 4, `Cashier` would take on both pricing logic and payment settlement, reducing cohesion and forcing changes to `Cashier` whenever billing rules evolve.


* **Loose Coupling and Dependency Management:** A central goal of OO design is managing dependencies between classes to minimize the impact of change. At Step 2, `Waiter` already holds references to both the `Customer` and the selected `MenuItem` when creating the `Order`. Resolving `customer.applyCustomerDiscount(item.getPriceCents())` at that moment means `Order` stores a single final integer-cent value, and `Cashier` only needs to query that locked amount at Step 4 without depending on `MenuItem` base prices, `Customer` discount hooks, or pricing policies.


* **Encapsulation and Point-in-Time Transaction Integrity:** Proper encapsulation ensures an object protects its own data and invariants through well-defined methods. In the restaurant domain, a bill is fixed when the order is placed rather than dynamically recalculated several turns later after cooking, serving, and eating. Storing the computed amount in a private field (such as `lockedPriceCents`) inside `Order` with no public mutator guarantees that the price cannot be altered mid-lifecycle.


* **Open-Closed Principle (OCP) and Readiness for Time-Sensitive Rules:** Designing modules to be open for extension but closed for modification allows future rules to plug in without rewriting stable classes. Even when building Stage 2 before Stage 3 requirements are known, locking the price at order creation cleanly decouples *when* a price is determined from *when* it is paid. When Stage 3 later introduces `HappyHourPolicy`—which grants 20% off orders taken while active (applied after the VIP discount) without affecting orders already placed—locking the price at Step 2 satisfies that rule automatically without requiring `Cashier` to track historical turn states or undergo any modification.


* **Single Source of Truth Across Turns:** Calculating and rounding the integer-cent bill once upon `Order` creation ensures that any mid-game status displays and the final revenue tally collected by `Cashier` read the exact same figure, eliminating duplicate arithmetic and potential rounding discrepancies.