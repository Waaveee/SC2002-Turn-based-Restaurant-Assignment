# Stage 3 Conversation (JM)

## User

Now at stage 3, which behavior from stage 2 should I test again?

## Assistant

At Stage 3, test that the **Stage 2 customer behaviours still work after adding the Host, ComboMeal and Happy Hour**. These are regression tests: they check that earlier features still behave correctly after changes.

| Stage 2 behaviour | What to check in Stage 3 |
|---|---|
| **Customer arrivals** | The base schedule still has six arrivals, including at least one VIP and one Critic. |
| **Waiting satisfaction** | Regular, VIP and Critic customers still lose **8, 5 and 12 points**, respectively, per waiting turn. Test without Happy Hour active so recovery doesn’t affect the result. |
| **VIP discount** | A VIP still receives **10% off** when Happy Hour is inactive. For example, a $15 Burger should cost **$13.50**. |
| **Critic preference** | A Critic still chooses **Pasta**, including after the combo has been added to the menu. |
| **Regular customer bill** | A Regular customer pays the full item price when Happy Hour is inactive. |
| **Common service flow** | All three customer types can still be seated, order, be served and pay. |
| **Abandonment** | A customer whose satisfaction reaches zero still leaves without paying, and their unfinished order is cancelled. |
| **Victory and defeat** | The game still runs for 18 turns and evaluates the same three victory targets. |

For interactions between the old and new features, also test a **VIP order during Happy Hour**: the VIP discount should be applied first, then the Happy Hour discount, with the final price locked.

You don’t need to expect every Stage 3 run to have the same final result as Stage 2: using the Host, Happy Hour or combo can change the outcome. Compare equivalent choices when checking unchanged behaviour, and record the **actual observed result** for each test.
