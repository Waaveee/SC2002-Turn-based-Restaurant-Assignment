\# Stage 2 Conversation (Vivaan)



\## User



Compared with Stage 1, what new behaviour did Stage 2 add (for example the Host, VIP customers, the Critic or staff tasks)? Which Stage 1 classes did I have to extend, and which did I leave alone?



\## Assistant



\### New Behavior Added in Stage 2



Stage 2 focuses specifically on extending customer behavior while keeping the core turn-based engine intact. Here is what was added:



\* \*\*New Customer Types:\*\* Stage 2 adds \*\*VIP Customers\*\* and \*\*Critic Customers\*\* alongside the existing regular customers.



\* \*\*VIP Customer Traits:\*\* VIPs lose 5 satisfaction points per waiting turn (compared to 8 for regular customers) and receive a 10% discount on their bill.



\* \*\*Critic Customer Traits:\*\* Critics lose 12 satisfaction points per waiting turn, pay full price, and exhibit a distinct order preference or reaction to service that your group defines and documents.



\* \*\*Arrival Adjustments:\*\* A reproducible base-setting run in Stage 2 must include at least one VIP and one Critic, replacing two of the regular customer arrivals while keeping the total arrival count at six.



\* \*\*Polymorphic Dispatch:\*\* While all customer types follow the exact same order and service flow, their unique behaviors must be dispatched dynamically through the customer objects rather than through a chain of type-check statements in the turn controller.



\*(Note: Features such as the \*\*Host\*\*, \*\*Combo Meals\*\*, and \*\*Happy Hour\*\* belong to \*\*Stage 3\*\*, not Stage 2).\*



\### Class Extensions and Modifications



Based on the project design requirements and object-oriented principles:



\* \*\*Extended Classes:\*\* The base \*\*`Customer`\*\* class from Stage 1 is extended (or refactored into an abstract class/superclass) to create `VIPCustomer` and `CriticCustomer` subclasses, allowing polymorphic behavior for satisfaction loss, billing discounts, and preferences.



\* \*\*Left Alone / Core Classes:\*\* Core components like the `Waiter`, `Chef`, `Cashier`, `Table`, and `Order` classes remain largely unchanged in their primary operational logic, as all customer variants follow the same underlying order and service pipeline. Any minor updates to these classes are typically restricted to small registration or setup changes needed to accommodate the new customer subclasses.



\## User



Why is it a good design to put the shared behaviour in an abstract Staff class and extend it, instead of copying code into Host, Waiter and Chef? What would break or get messy otherwise?



\## Assistant



Introducing an abstract `Staff` class (as you build toward or refine your architecture for \*\*Stage 3\*\* and its new `Host` role) is a prime example of leveraging object-oriented principles.



Here is why using an abstract `Staff` base class with inheritance is a strong architectural choice, and what would happen if you chose code duplication instead.



\### Why an Abstract `Staff` Class is Great Design



1\. \*\*Enforces Code Reuse (The DRY Principle):\*\*



&#x20;  \* Even though a `Waiter`, `Chef`, and `Host` have distinct responsibilities (e.g., Waiters handle seating/taking orders/serving, Chefs advance preparation units, and Hosts seat queued customers), they share common traits. They all occupy staff slots, have identifiers or names, track whether they have performed their action for the current turn, and require status logging. Putting these shared attributes and utility methods in a `Staff` superclass eliminates redundant code.



2\. \*\*Enables Polymorphic Control in the Turn Loop:\*\*



&#x20;  \* With an abstract `Staff` reference (or a shared interface contract), your game controller or turn manager can invoke generic actions—such as a `resetTurnState()` or an execution hook—across all staff members in a uniform loop. The controller doesn't need to know the specific subclass type to manage turn-based constraints.



3\. \*\*Aligns with the Open-Closed Principle (OCP):\*\*



&#x20;  \* The project requirements emphasize designing systems that gracefully absorb extensions across stages without rewriting stable code. By establishing `Staff` as an abstract base, if a future update requires adding a new property or method to \*all\* personnel (e.g., tracking total tasks completed or shift fatigue), you only update the single `Staff` class rather than modifying `Waiter`, `Chef`, and `Host` individually.



\### What Would Break or Get Messy Otherwise?



If you copied code across `Host`, `Waiter`, and `Chef` instead of using inheritance, you would run into several classic software engineering pain points:



\* \*\*Massive Code Duplication \& Divergence Bugs:\*\*



&#x20; If turn-tracking flags, validation checks, or state-reset logic are copy-pasted into every class, fixing a bug in one staff member means you have to manually remember to fix it across all of them. Inevitably, one class gets out of sync, leading to subtle, hard-to-trace bugs.



\* \*\*Controller Pollution (Type-Checking Nightmares):\*\*



&#x20; Without a common superclass, your turn controller would likely resort to messy `instanceof` checks or long `switch-case` blocks to handle staff actions. This tightly couples your control logic to concrete implementations, directly violating polymorphic design goals and cluttering your codebase.



\* \*\*Poor Marks on Design Principles:\*\*



&#x20; For the SC2002 assessment, 15% of your grade relies on \*\*Design considerations\*\* (cohesion, extensibility, and principles like OCP and polymorphism). Relying on copy-pasted boilerplate instead of inheritance makes it much harder to defend your class diagram and design discussion report during evaluations.

