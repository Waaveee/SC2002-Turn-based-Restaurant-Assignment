\# Stage 3 Conversation (Vivaan)



\## User



Stage 3 adds Happy Hour and the Combo Meal on top of Stage 2. How does each one plug into the existing turn flow, and which classes had to change to support them?



\## Assistant



In \*\*Stage 3\*\*, the game introduces \*\*Happy Hour\*\*, \*\*Combo Meals\*\*, and a new \*\*Host\*\* role, integrating them into the existing turn flow while expanding the object-oriented architecture.



\### How Stage 3 Features Plug into the Turn Flow



\#### 1. Happy Hour



\* \*\*Activation:\*\* The manager can activate Happy Hour \*\*once per game\*\* at the start of a turn, and it remains active for that turn and the next (lasting 2 turns total). Its remaining duration is displayed each turn.



\* \*\*Turn-Start Satisfaction Recovery:\*\* During each active turn, any customer who is still waiting for a table or waiting for their food recovers \*\*15 satisfaction points\*\* at the start of the turn (capped at a maximum of 100).



\* \*\*Order Pricing \& Discounting:\*\* Any orders taken while Happy Hour is active receive a \*\*20% discount\*\*. The price is locked in when the order is taken, and the policy does not retroactively change already placed orders or affect customers who have already left. If a VIP customer orders during Happy Hour, the VIP discount is applied first, followed by the Happy Hour discount (e.g., a VIP ordering a Burger: $\\$15 \\times 0.9 \\times 0.8 = \\$10.80$).



\#### 2. Combo Meal



\* \*\*Composition:\*\* A combo meal is made up of two existing menu items.



\* \*\*Kitchen \& Pricing Mechanics:\*\* Its preparation units equal the sum of its two components, and its price is \*\*90% of the components' combined price\*\*, rounded to the nearest cent (e.g., a Salad + Pasta combo costs $(\\$9 + \\$18) \\times 0.9 = \\$24.30$ and requires $1 + 2 = 3$ preparation units).



\* \*\*Customer Choice:\*\* Customers choose a combo according to a documented rule, after which the order flows through the standard kitchen, serving, and eating lifecycle.



\#### 3. Host Role



\* \*\*Seating Integration:\*\* The Host operates alongside the Waiter, seating \*\*at most one queued customer per turn\*\* independently of the Waiter's assigned task. The Waiter can still seat customers if needed, but the Host provides an extra front-of-house boost.



\### Classes That Had to Change or Be Added



To support these new features cleanly without breaking earlier behavior, several classes and components required updates or creation:



\* \*\*`Menu` / `MenuItem` / `Order`:\*\* Expanded or subclassed to support `ComboMeal`, handling the aggregation of two items, summing their preparation units, and applying the 90% pricing calculation.



\* \*\*`Customer`:\*\* Updated to incorporate the decision rule for choosing a combo meal alongside regular individual items.



\* \*\*Game Controller / Turn Manager / Manager:\*\* Modified to track Happy Hour state (activation flag and 2-turn remaining duration), trigger the +15 satisfaction recovery at the start of active turns for eligible customers, and apply the 20% discount logic when orders are placed.



\* \*\*`Host` (New Class):\*\* Added as a new staff member (inheriting from or working alongside the `Staff` structure) to execute independent customer seating during the turn's action steps.



\## User



How does the order price get worked out when a VIP discount and Happy Hour both apply? Why is it better to lock the price inside the Order object when the order is taken?



\## Assistant



\### How the Combined Discount Is Calculated



When a VIP customer places an order while Happy Hour is active, the discounts are applied sequentially rather than added together:



1\. \*\*VIP Discount First:\*\* The customer's 10% VIP discount is applied to the base menu price first (multiplying by $0.9$).



2\. \*\*Happy Hour Discount Second:\*\* The 20% Happy Hour discount is then applied to that discounted amount (multiplying by $0.8$).



3\. \*\*Currency Handling:\*\* Amounts are stored in integer cents to prevent floating-point rounding drift and displayed in dollars to two decimal places.



$$\\text{Final Price} = \\text{Base Price} \\times 0.9 \\times 0.8$$



For example, if a VIP orders a \\$15.00 Burger during Happy Hour, the calculation is $\\$15.00 \\times 0.9 \\times 0.8 = \\$10.80$ (an effective 28% total discount).



\### Why Locking the Price Inside `Order` Is Better Design



Capturing and freezing the final price inside the `Order` object at the exact moment the Waiter takes the order solves both gameplay rules and software architecture concerns:



\* \*\*Enforces Time-Sensitive Game Rules:\*\* Happy Hour only lasts for two turns, and its discount applies strictly to orders \*taken\* while it is active—it must not retroactively change orders placed earlier, nor lose the discount if Happy Hour expires before the customer finishes eating and pays. Locking the price on creation guarantees that payment later in the game matches the exact conditions when the order was placed.



\* \*\*Decouples the `Cashier` from Pricing Logic:\*\* When the customer reaches `READY\_TO\_PAY` turns later, the `Cashier` simply reads the finalized amount from the `Order` object and adds it to revenue. The `Cashier` does not need to check whether Happy Hour \*was\* active several turns ago or re-query customer discounts.



\* \*\*Prevents State-History Bugs (Encapsulation):\*\* Without locking the price, the system would have to store historical snapshots of when each order was taken and cross-reference them against Happy Hour activation turns during checkout. Encapsulating the final price inside `Order` keeps the `Order` self-contained and avoids fragile temporal dependencies across turns.

