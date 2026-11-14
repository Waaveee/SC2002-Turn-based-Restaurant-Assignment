# Stage 1 Conversation (JM)

## User

for stage 1, how to decide which customer pay first in the event where two customer want to pay at the same time, or is it even possible that two customer will pay at the same time due to the requirements of the game?

## Assistant

In **Stage 1, two customers cannot pay in the same turn**: there is only one Cashier, and the Cashier handles at most one payment per turn.

Under the current rules, it’s also unlikely for a payment queue to build up—and in the normal Stage 1 flow, **two customers cannot become ready to pay on the same turn**. The Waiter can serve only one customer per turn, and each customer finishes eating one turn after being served. The Cashier then automatically handles an eligible payment each turn.

For example:

| Turn | Customer A | Customer B |
|---|---|---|
| 4 | Served | — |
| 5 | Finishes eating and becomes READY_TO_PAY | Served |
| 6 | Pays | Finishes eating and becomes READY_TO_PAY |
| 7 | — | Pays |

So at the end of Turn 6, A has already paid and B is ready to pay. The payment step happens before the game marks newly finished customers as ready.

The code nevertheless has a rule for selecting between multiple eligible customers, if that situation ever arises:

1. Choose the customer who became `READY_TO_PAY` **earliest**.
2. If they became ready on the same turn, choose the customer with the **lower customer ID**, which corresponds to earlier arrival.
