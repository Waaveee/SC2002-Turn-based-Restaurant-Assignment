
## Question
Breakdown the project and explain the key tasks to be completed


## Reply
**SC2002 Restaurant Rush** is an offline, command-line, turn-based restaurant management game in Java built across three progressive GitHub stages, where 80% of the 100-mark grade comes from object-oriented design, UML diagrams, and documented evolution rather than raw code volume.

| Deliverable Workstream | Rubric Weightage | Core Completion Criteria |
| --- | --- | --- |
| **1. Progressive Java Implementation (`stage-1` to `stage-3`)** | **20 Marks** | Three standalone runnable Git tags implementing the 18-turn game loop, polymorphic customers, `Host`, `ComboMeal`, and `HappyHourPolicy`.

 |
| **2. UML Class Diagrams (3 Stages) & Sequence Diagram (1 Use Case)** | **45 Marks** (`25 + 20`) | Three stage-specific Class Diagrams matching each stage's code, plus one Stage 2/3 Sequence Diagram with guards, loops, and use case specs.

 |
| **3. Design Consideration & SOLID Report Chapters** | **15 Marks** | Key abstractions table, SOLID applications (where genuinely reflected), design trade-offs, and an alternative design considered.

 |
| **4. Git Evolution, AI Logs, Video & Individual Reflections** | **20 Marks** | Stage diff analysis, personal GitHub branches with full AI chat logs, 5 individual reflections, and a ~15-minute recorded video.


---

### Workstream 1: Progressive Software Implementation (20 Marks)

You must develop and tag the software sequentially—treating each stage as if later requirements are unknown—so that earlier tags contain zero future-stage features.

* **Task 1.1 — Stage 1 Baseline Engine (`stage-1`):**
* Implement the reproducible base setting: 18 turns maximum, 2 tables, 1 Waiter (1 task/turn), 1 Chef (1 prep unit/turn), 1 Cashier (1 payment/turn), and 6 regular customer arrivals at the start of turns 1, 4, 7, 10, 13, and 16.


* Implement the three base menu items (`Salad`: $9.00 / 1 unit, `Burger`: $15.00 / 1 unit, `Pasta`: $18.00 / 2 units) using integer cents for all monetary storage and deterministic/seeded food selection.


* Enforce the strict 6-step turn sequence and turn-boundary delays: Waiter acts before Chef (dishes finished to `READY` this turn are served next turn at earliest), eating takes one full turn (`SERVED` $\rightarrow$ `READY_TO_PAY`), Cashier pays oldest `READY_TO_PAY` customer next turn at earliest, and unserved waiting/seated customers lose 8 satisfaction per turn (abandoning at 0 without paying).


* Separate boundary I/O from domain/control rules (`Entity–Control–Boundary`), handle invalid inputs without consuming staff tasks, evaluate Turn 18 Victory (`>= 4` paid customers, `>= $45` revenue, `>= 60` average paid satisfaction), and document 1 reproducible Victory and 1 Defeat run.




* **Task 1.2 — Stage 2 Customer Behaviour Extension (`stage-2`):**
* Add `VIPCustomer` (loses 5 satisfaction per waiting turn, receives 10% bill discount) and `CriticCustomer` (loses 12 satisfaction per waiting turn, plus a documented order preference or service reaction).


* Dispatch all customer-specific behaviour polymorphically through `Customer` objects rather than `if/else` or `switch` type-checking chains in the turn controller.


* Replace two regular arrivals in the base schedule with at least one `VIPCustomer` and one `CriticCustomer` while maintaining six total arrivals.




* **Task 1.3 — Stage 3 Role & Menu Policy Extension (`stage-3`):**
* Add `Host`, who seats at most one queued customer per turn in addition to the Waiter's single task.


* Add `ComboMeal`, composing two existing menu items with preparation units equal to their sum and price equal to 90% of their combined price rounded to the nearest cent, alongside a documented selection rule.


* Add `HappyHourPolicy`, activated once per game at turn start for that turn and the next: unserved waiting/seated customers recover 15 satisfaction at turn start (capped at 100), and orders placed while active lock in a 20% discount applied *after* any VIP discount.





---

### Workstream 2: UML Modeling & Diagrams (45 Marks)

* **Task 2.1 — Three Stage-Specific UML Class Diagrams (25 Marks):**
* Produce three separate UML Class Diagrams corresponding strictly to `stage-1`, `stage-2`, and `stage-3` code.


* Display `<<boundary>>`, `<<control>>`, and `<<entity>>` stereotypes, enums, attribute/operation visibility (`-`, `#`, `+`), inheritance, and associations with exact multiplicities.


* Directly annotate concrete examples of **encapsulation**, **polymorphism**, and **composition** on the diagrams.




* **Task 2.2 — One Complex UML Sequence Diagram (20 Marks):**
* Model one complete Stage 2 or Stage 3 scenario where a manager choice changes an outcome, explicitly stating the stage, starting conditions, and outcome.


* Show message flows across the CLI boundary, turn controller, staff, and domain entities—covering customer arrival, ordering, Waiter/Chef actions, `READY` $\rightarrow$ `SERVED`, eating, payment, Turn 18 result check, and Stage 2/3 behaviour—using `opt`, `alt`, and `loop` combined fragments.





---

### Workstream 3: GitHub Repository & AI Communication Logs

* **Task 3.1 — Runnable Stage Tags & Diff Documentation:**
* Push runnable commits on `main` tagged `stage-1`, `stage-2`, and `stage-3`.


* In `README.md` and the report, compare `stage-1` $\rightarrow$ `stage-2` and `stage-2` $\rightarrow$ `stage-3`, listing new classes, modified/deleted classes with architectural justifications, regression checks confirming earlier behaviour still works, and GitHub compare links.




* **Task 3.2 — Individual AI History Branches:**
* Each of the 5 members must create their own personal branch in the group GitHub repository and upload their complete, chronological AI prompts, replies, and follow-ups organized by stage (e.g., `ai-history/stage-1/chat-01.md`), as summaries alone are insufficient.


* Link every stage tag and every member's AI history branch in `README.md`.





---

### Workstream 4: Group PDF Report & Individual Reflections (15 Marks + Process)

* **Task 4.1 — Architectural & SOLID Design Discussion (15 Marks):**
* Include a table of key abstractions and their responsibilities.


* Explain how SRP, OCP, LSP, ISP, and DIP are applied (only where genuinely reflected in code), discuss design trade-offs, and analyze one alternative design considered.




* **Task 4.2 — Custom Rules & Functional Evidence:**
* Document your deterministic/seeded food choice rule, Critic special behaviour, and ComboMeal choice rule, alongside reproducible Victory and Defeat run results demonstrating how manager choices alter outcomes.




* **Task 4.3 — Individual Reflection Chapter (5 Named Sections):**
* Each member writes a dedicated section under their own name answering four prompts with concrete project examples: what they learned, how they applied course concepts, what challenged them most, and how they solved it.





---

### Workstream 5: Recorded Video & Final Archive Packaging

* **Task 5.1 — 15-Minute Recorded Presentation & README Timestamps:**
* Record one ~15-minute video with clear screen capture and audible explanations where every member presents a part they understand.


* Cover all 7 required segments and list their timestamps in `README.md`: (1) launching the submitted project, (2) meaningful manager choices under staff constraints, (3) dish going `READY` $\rightarrow$ `SERVED` $\rightarrow$ payment, (4) VIP or Critic behaviour, (5) a Stage 3 feature, (6) one Victory and one Defeat run, and (7) stage tag comparisons and UML diagrams, plus individual contributions and learnings.




* **Task 5.2 — Final Zip Archive & Declarations (`LABGroup_AssignmentGrp.zip`):**
* Package Java source code, `README.md`, PDF report, video file (or accessible download link), signed group Declaration of Original Work, and all 5 individual signed Declarations on Use of GAI Assistance into a single zip file (`LABGroup_AssignmentGrp.zip`) for NTULearn submission by **Sunday, 15 November 2026, 11:59 PM SGT**.