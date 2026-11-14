# Stage 1 Conversation (Vivaan)

## User

hey do you understand how the project works?

## Assistant

Yes, absolutely! I have a complete grasp of **Restaurant Rush**, the SC2002/SC2302 Java command-line group project. 

Here is a quick rundown of how the project is structured and what we need to nail down, especially with the deadline coming up on **Sunday, November 15, 2026, at 11:59 PM SGT**:

### 1. Core Mechanics & Base Setting (Stage 1 Baseline)
* **The Environment:** A turn-based management game running for a strict maximum of **18 turns** with **2 tables**.
* **Staff:** 1 Waiter (1 task/turn), 1 Chef (1 prep unit/turn), and 1 Cashier (1 payment/turn).
* **Arrivals:** 6 total customers arriving on turns 1, 4, 7, 10, 13, and 16.
* **Menu Items:** Salad ($9.00, 1 unit), Burger ($15.00, 1 unit), and Pasta ($18.00, 2 units).
* **Victory Criteria (at Turn 18):** At least 4 paid customers, total revenue $\ge \$45$, and an average paid customer satisfaction $\ge 60$.

### 2. The Three Progressive Stages
* **Stage 1 (`stage-1`):** Establishes the core baseline engine with Regular customers, basic menu items, staff constraints, the full turn sequence (Arrivals $\rightarrow$ Manager Choices $\rightarrow$ Kitchen/Serving $\rightarrow$ Eating/Payment $\rightarrow$ Waiting/Abandonment $\rightarrow$ Result), and both victory/defeat logic.
* **Stage 2 (`stage-2`):** Extends customer behavior polymorphically by adding **VIP Customers** (5 satisfaction loss/turn, 10% bill discount) and **Critics** (12 satisfaction loss/turn, custom order preferences/reactions) without cluttering the turn controller with type checks.
* **Stage 3 (`stage-3`):** Introduces the **Host** (seats up to 1 queued customer per turn independently of the Waiter), **Combo Meals** (bundles two items at 90% combined price and summed preparation units), and **Happy Hour** (activates once for 2 turns, giving 20% off new orders and +15 satisfaction recovery to waiting/seated customers).

### 3. Key Deliverables & Submission Requirements
* **Code & GitHub:** Runnable tagged commits (`stage-1`, `stage-2`, `stage-3`).
* **UML Diagrams:** 3 class diagrams (one per stage matching the code) and 1 sequence diagram illustrating a complex use case from Stage 2 or 3.
* **Group Report:** Details the design evolution, SOLID principles applied, trade-offs, and **individual reflections**.
* **AI Chat Logs:** Full conversation logs saved in your personal branch under `ai-history/stage-1/`, `ai-history/stage-2/`, and `ai-history/stage-3/`.
* **Video Presentation:** A ~15-minute recorded walk-through with precise timestamps listed in the README covering the game launch, staff constraints, dish lifecycle, customer variants, Happy Hour/Host features, victory/defeat runs, and stage transitions.

---

How can I help you right now? Whether you need to refine code across the stages, check your UML class/sequence diagram logic, structure the change evidence report, write your individual reflection, or organize your AI history logs, just let me know!

## User

thanks, In our Stage 1 game, what is the 18-turn loop, and what does each turn do, in order? Who are the main classes (Customer, Waiter, Chef, Manager and so on) and what is each one responsible for?

## Assistant

In Stage 1 of **Restaurant Rush**, the game operates on a maximum of **18 turns** with 2 tables, 1 Waiter, 1 Chef, and 1 Cashier. The game always runs all 18 turns to completion (there is no early ending), and victory is determined at the end of turn 18 based on performance targets.

---

### The 6-Step Turn Order
Every single turn executes these six steps strictly in order:

1. **Arrivals:** The scheduled customer for that turn joins the waiting queue with a starting satisfaction of 100.
2. **Manager Choices:** 
   * **Waiter:** The manager assigns the Waiter *at most one task*: to Wait, seat one queued customer at a free table, take an order from one seated customer, or serve one `READY` order.
   * **Chef:** The manager assigns the Chef *at most one order* to advance by one preparation unit (the Chef is allowed to work on an order that the Waiter just took during the same turn).
3. **Kitchen and Serving:** The Waiter acts before the Chef. When an order's final preparation unit is completed, it becomes `READY`. A dish can only be served if it was *already* `READY` when the Waiter acts, meaning dishes finished this turn cannot be served until the next turn at the earliest. Serving changes the order status to `SERVED`.
4. **Eating and Payment:** A customer who was served on a previous turn finishes one full turn of eating and becomes `READY_TO_PAY`. The Cashier automatically processes at most one `READY_TO_PAY` customer per turn (oldest first). Payment adds the bill to total revenue, marks the customer and order as `PAID`, and frees the table. (Customers who become `READY_TO_PAY` this turn pay next turn at the earliest).
5. **Waiting and Abandonment:** At the end of the turn, any waiting or seated customer who has *not* yet been served loses satisfaction (Regular customers lose 8 points, down to a minimum of 0). If a customer's satisfaction hits 0, they abandon the restaurant without paying—canceling any unfinished order and freeing the table. Customers who are already served, eating, or ready to pay lose no satisfaction.
6. **Result:** Updates metrics. The game evaluates victory/defeat only after turn 18 concludes.

---

### Main Classes and Their Responsibilities

Following object-oriented design principles, the game cleanly separates rules and domain logic from the user interface (keeping rules out of the UI):

* **`Customer` (specifically Regular Customers in Stage 1):** 
  * Tracks individual customer state (satisfaction score starting at 100, current stage in the dining lifecycle).
  * Chooses food items independently using a documented deterministic rule or seeded randomness once their order is taken.
* **`Waiter`:** 
  * Manages staff constraints for front-of-house operations. 
  * Responsible for executing at most one task per turn: seating customers, taking orders, or serving `READY` dishes.
* **`Chef`:** 
  * Manages kitchen operations. 
  * Responsible for advancing the preparation units of an active order by 1 unit per turn.
* **`Cashier`:** 
  * Manages automated checkout. 
  * Responsible for automatically settling bills for at most one `READY_TO_PAY` customer per turn, updating revenue, marking transactions as paid, and freeing up tables.
* **`Table`:** 
  * Represents physical seating resources (2 tables in the base setting). 
  * Maintains its occupancy status, staying occupied until its assigned customer either pays or leaves.
* **`Order`:** 
  * Encapsulates menu item details (e.g., Salad, Burger, Pasta), required preparation units, and current order status (`PENDING`, `READY`, `SERVED`, `PAID`, or cancelled).
* **Waiting Queue / Queue Structure:** 
  * Holds customers who have arrived but cannot yet be seated due to occupied tables.
* **Manager / Game Controller / CLI:** 
  * The player acts as the manager. 
  * The command-line interface (CLI) is responsible solely for collecting manager inputs and displaying the current game state, while domain controllers enforce the underlying business rules.

## User

Following on from that, where does the game decide that a customer is satisfied or abandons? How do those satisfaction changes feed into the win and lose conditions?

## Assistant

### Where Satisfaction and Abandonment Are Decided

The game evaluates satisfaction changes and customer abandonment during **Step 5 (Waiting and abandonment)**, which occurs at the end of every turn:

* **Satisfaction Loss:** Any customer who is waiting in the queue or seated at a table but has *not* yet been served loses satisfaction (Regular customers lose 8 points per turn, down to a minimum score of 0). 
* **Abandonment Trigger:** If a customer's satisfaction reaches 0 during this step, they abandon the restaurant without paying. This automatically cancels any unfinished order and frees up their table.
* **Exemptions:** Customers who have already been served, are currently eating, or are ready to pay do not lose any satisfaction.

---

### How Satisfaction Feeds into Win and Lose Conditions

The game always runs through all 18 turns to completion with no early endings, and satisfaction scores directly impact whether the player achieves victory at the end:

* **Recording Satisfaction:** When a customer successfully pays their bill (during Step 4), their final satisfaction score at that moment is recorded.
* **The Victory Criteria:** At the end of turn 18 (Step 6), the game evaluates three mandatory targets:
  1. At least **4 paid customers**.
  2. Total revenue of **at least $45**.
  3. The **average satisfaction of all paid customers must be at least 60**.
* **Win/Lose Outcome:** If all three criteria are satisfied, the game results in a **Victory**; otherwise, it results in a **Defeat**. Keeping customer wait times low and preventing abandonments is crucial to maintaining that average satisfaction threshold of 60.
