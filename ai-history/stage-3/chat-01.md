## Question
Does the UML sequence diagram cover the requirements as stated in the assignment brief? 


## Reply
Yes—100%. Your Sequence Diagram (SC2002-UML Sequence Diagram.drawio.png) covers every single requirement in the project briefing, and actually goes above the minimum by showing Stage 2 (VIPCustomer) and Stage 3 (Host + HappyHourPolicy) working together.

Here is the exact checklist mapping each requirement from the brief to the messages and fragments in your diagram:

| Project Brief Requirement | Covered? | Exact Location in Your Sequence Diagram |
| --- | :---: | --- |
| 1. One complete scenario in which a manager choice changes an outcome | Yes | Tracks the Turn 4 `VIPCustomer` from arrival to Turn 18 Victory. Shows how the Manager's choices to activate Happy Hour (Msg 6), seat with the Host (Msg 14), take the order with the Waiter (Msg 24), and cook with the Chef (Msg 35) on the same turn compress the pipeline, recover satisfaction to `100` (Msg 45), free Table 1 on Turn 7 (Msg 68), and achieve Victory (Msg 70–73). Also shows an invalid Manager choice being rejected and retried without losing the Waiter's turn (Msg 20–23). |
| 2. Customer arrival and order | Yes | • Arrival (Turn 4): Msg 2–4 (`processArrivals(turn = 4)` →\\rightarrow→ `new VIPCustomer(id = 2)` →\\rightarrow→ `addCustomerToQueue(vip)`) and Msg 16–18 (`seatCustomer` →\\rightarrow→ `markSeated()`).<br>• Order (Turn 4): Msg 24–33 (`takeOrder(vip, menu, happyHour)` →\\rightarrow→ `chooseFood(menu, choiceStrategy)` →\\rightarrow→ `new Order(vip, burger, 1080)` →\\rightarrow→ `placeOrder(order)`). |
| 3. Waiter and Chef assignments | Yes | • Waiter (Turn 4 & Turn 5): Prompted via `promptWaiterTask(restaurant)` (Msg 20, 46), assigned `takeOrder(...)` (Msg 24) on Turn 4 and `serveOrder(...)` (Msg 49) on Turn 5.<br>• Chef (Turn 4): Prompted via `promptChefTask(restaurant)` (Msg 34–36), assigned `prepareOrder(order)` (Msg 37). |
| 4. `READY` then `SERVED` | Yes | • `READY` (Turn 4): Msg 38–39 (`advancePreparation()` sets `remainingPrepUnits = 0, status = READY`; cannot be served on Turn 4 because the Waiter already acted before the Chef).<br>• `SERVED` (Turn 5): Msg 49–52 (`serveOrder(order, currentTurn = 5)` →\\rightarrow→ `markServed(currentTurn = 5)` on `order:Order` →\\rightarrow→ `markServed()` on `vip:VIPCustomer`). |
| 5. Eating | Yes | • Turn 6: Msg 57–60 (`transitionEatenCustomers(currentTurn = 6)` →\\rightarrow→ `hasFinishedEating(currentTurn = 6)` returns `true` →\\rightarrow→ `finishEating(currentTurn = 6)` transitions `vip:VIPCustomer` to `READY_TO_PAY`). Also shows why the Cashier returns `null` earlier in Turn 6 (Msg 54–56). |
| 6. Payment | Yes | • Turn 7: Msg 62–69 (`processPayment(restaurant)` →\\rightarrow→ `getOldestReadyToPayCustomer()` returns `vip` →\\rightarrow→ `markPaid()` on `order:Order` (Msg 65) and `vip:VIPCustomer` (Msg 66) →\\rightarrow→ `recordPayment(1080, 100)` (Msg 67) →\\rightarrow→ `freeTable()` (Msg 68)). |
| 7. The result check | Yes | • Turn 18: Msg 70–73 (`checkVictory()` →\\rightarrow→ `evaluateVictoryTargets()` returns `true` →\\rightarrow→ `displayFinalResult(true, restaurant)`). |
| 8. One Stage 2 or 3 behaviour (e.g. VIP pricing or Happy Hour) | Yes (3 shown) | Includes all three:<br>1. Stage 2 VIP Pricing & Decay: `applyCustomerDiscount(1500)` →\\rightarrow→ `1350` (Msg 27–28) and `applyEndOfTurnDecay()` (Msg 41–42).<br>2. Stage 3 Happy Hour: `activate()` (Msg 8), `+15` `recoverSatisfaction(15)` (Msg 11, 45), and `calculateDiscountedPrice(1350)` →\\rightarrow→ `1080` (Msg 29–30).<br>3. Stage 3 Host Seating: `host.seatCustomer(vip, table1)` (Msg 16–19). |
| 9. Messages among CLI, controller, staff and domain objects, with guards & loops | Yes | • CLI (Boundary): `:RestaurantUI`<br>• Controllers: `gameMgr:GameMgr`, `happyHour:HappyHourPolicy`<br>• Staff: `host:Host`, `waiter:Waiter`, `chef:Chef`, `cashier:Cashier`<br>• Domain Objects: `restaurant:Restaurant`, `vip:VIPCustomer`, `table1:Table`, `order:Order`<br>• Guards & Loops: `opt [activateHH == true && happyHour.canActivate()]`, `opt [happyHour.isActive() == true]`, `loop [c in unservedCustomers]`, `loop [validWaiterChoice == false]`, `alt [cmd is invalid] / [Manager chooses Take Order from VIP]`, and `opt [happyHour.isActive() == true]` inside `takeOrder`. |


