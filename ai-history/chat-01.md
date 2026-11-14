# Class Diagram Guide

## Question

Can you generate an outline or guide me through for the class diagram? Give me suggestions and not simply just giving me the answer

## Reply

Here's a step-by-step guide. For each step I give you the questions to ask yourself, and you work out the answers from the brief. I'm not listing the classes for you.

### Step 0: Know what the marker looks for

The brief says the class diagram is worth 25 marks. It is marked on:
- coverage
- relationships and notation
- attributes, operations and encapsulation
- clarity and match to the code

You need three diagrams, one per stage. Each diagram must match the code at that stage's tag, and the final diagram must match the Stage 3 code.

### Step 1: Find the candidate classes (Stage 1 only)

Read Sections 2 and 3 of the brief and underline every noun.
- Which nouns have their own data and behaviour? Those are classes.
- Which nouns are only values, like a price or a satisfaction score? Those are probably attributes.
- Which nouns have a fixed set of values, like the states a customer or order can be in? Think about enums.
- Is any noun really something that does work, like "runs the turns" or "reads input"? Ask whether that is a control or boundary class.

### Step 2: Give each class one job

For each candidate, finish the sentence "This class is responsible for ___." If you need "and" more than once, it is probably doing too much.
- Where should the game rules live? The brief says to keep rules out of the UI.
- Who should own the state of the restaurant, such as revenue and the queue?
- Who decides what a customer orders? The brief hints this should be swappable.

### Step 3: Decide the relationships (the hardest part)

Take each pair of related classes and ask these in order:
1. Is A really a kind of B? If yes, use inheritance. If no, don't force it.
2. Does A own B, so that B disappears when A does? That points to composition.
3. Does A only hold a reference to B, which exists on its own? That points to aggregation or a plain association.
4. Does A only use B briefly, as a parameter or a local object? That is a dependency.
5. Is it one-to-one, one-to-many or optional? Write the multiplicities from the brief's numbers, such as 2 tables and 6 arrivals.

A test: which of your inheritance arrows would still make sense if you read them aloud as "is a"?

### Step 4: Decide what to show in each box
- Which attributes are private, and which really need to be protected? Why?
- What are the operations that other classes actually call? You don't need every getter.
- Which classes need to be abstract, and which methods should be abstract?
- Mark visibility (`+`, `-`, `#`) on everything. The brief asks for it.

### Step 5: Add Stages 2 and 3 as a change, not a redraw

Read Sections 4.2 and 4.3 of the brief. For each new requirement, ask:
- Can I handle this by adding a class and touching nothing else?
- If an old class has to change, why, and is the change small?
- Is any rule being decided by an `if (type == ...)` chain? The brief says that is not allowed for customer behaviour.
- Which new requirement should be a separate class and which should be an extra method?

Write down your answers as you go. They become the "what was added, what was modified and why" evidence the briefing asks for.

### Step 6: Add the annotations

The brief asks for one meaningful example each of encapsulation, polymorphism and composition.
- Which class hides data most clearly?
- Where does one method call behave differently depending on the object?
- Where does one object own and create another?

Pick the clearest examples and write a two-line note for each.

### Step 7: Check before you finish
- Does every class appear in the code, and every code class in the diagram?
- Do the multiplicities and visibilities match the code?
- Are Stage 1 and Stage 2 free of later features?
- Could a stranger read the diagram and tell the layers apart (UI, control, entity)?

### A suggested order
1. Sketch Stage 1 on paper with boxes and arrows only.
2. Add attributes and operations.
3. Put it in draw.io, tidy the layout and add multiplicities.
4. Copy the file, then add Stage 2, then do the same for Stage 3.
5. Add the annotations and compare with the code.

If you want, send me your Stage 1 class list and relationships when you have a first draft. I'll tell you which ones to question, without redrawing it for you.
