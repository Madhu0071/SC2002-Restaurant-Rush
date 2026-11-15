# SC2002-Restaurant-Rushn
# Restaurant Rush

SC2002 group project: a turn-based restaurant management game played in the terminal.
You manage a small restaurant for 18 turns: seat customers, take orders, cook and serve,
and keep customers happy enough to pay.

## Quick links
| | Link |
|---|---|
| Stage 1 code | https://github.com/Madhu0071/SC2002-Restaurant-Rushn/tree/stage-1 |
| Stage 2 code | https://github.com/Madhu0071/SC2002-Restaurant-Rushn/tree/stage-2 |
| Stage 3 code | https://github.com/Madhu0071/SC2002-Restaurant-Rushn/tree/stage-3 |
| Stage 1 → 2 changes | https://github.com/Madhu0071/SC2002-Restaurant-Rushn/compare/stage-1...stage-2 |
| Stage 2 → 3 changes | https://github.com/Madhu0071/SC2002-Restaurant-Rushn/compare/stage-2...stage-3 |
| Presentation video | https://drive.google.com/file/d/1yOZR8hCqLyde42ZEW-sXQ0B9QvpuzyLF/view?usp=drive_link |
| Group report | [docs/report.pdf](docs/report.pdf) |

## Presentation video
https://drive.google.com/file/d/1yOZR8hCqLyde42ZEW-sXQ0B9QvpuzyLF/view?usp=drive_link

### Contributions by stage
| Member | Stage 1 | Stage 2 | Stage 3 |
|---|---|---|---|
| Karthikeyan Madhumitha | Game loop, Restaurant, Table, ArrivalSchedule, ObjectiveEvaluator; repo setup and stage-1 tag | VIP and Critic arrivals in ArrivalSchedule; found and fixed the Critic review bug; stage-2 tag | GameController changes for Host, combos and Happy Hour; Happy Hour turns-left fix; test scripts; stage-3 tag |
| Coleman Tng Cheng Kai | [e.g. ManagerInput, RestaurantCLI, ScriptedInput, Menu, Dish] | [e.g. show customer type] | [e.g. ComboMeal, Happy Hour prompt and display] |
| Lam Jian Hern | [e.g. Customer, RegularCustomer, PricingService] | [e.g. VIPCustomer, CriticCustomer, Customer hooks] | [e.g. PromotionPolicy, HappyHourPolicy, PricingService.addPolicy] |
| Ho Jun De Dillon Trevor  | [e.g. Staff, Waiter, Chef, Cashier, StaffActions, Order] | [e.g. Cashier onPaid order] | [e.g. Host] |
| Tanieshka Amol Pawar | Stage 1 class diagram | Stage 2 class diagram | Stage 3 class diagram and sequence diagram |


| Time | Section | Presenter |
|---|---|---|
| 0:07 | Game launching from the project | Coleman |
| 0:56 | Meaningful manager choices, including a decision where staff are constrained |Coleman|
| 2:35 | A dish going from READY to SERVED to payment | Dillon |
| 3:45 | A VIP / Critic behaviour | Dillon |
| 5:33 | A Stage 3 feature | Jian Hern |
| 8:18 | One victory and one defeat (separate reproducible runs) | Jian Hern |
| 9:22 | Stage tag comparisons | Jian Hern |
| 10:36 | Diagrams | Tanieshka |

## How to compile and run
From the repo folder.

macOS / Linux:
```bash
rm -rf out
javac -d out src/restaurantrush/*.java
java -cp out restaurantrush.Main                          # play with the keyboard
java -cp out restaurantrush.Main tests/victory-stage3.txt # replay a saved script
```

Windows PowerShell:
```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
javac -d out src/restaurantrush/*.java
java -cp out restaurantrush.Main tests/victory-stage3.txt
```

To run an earlier stage, check out its tag first (`git checkout stage-1`), then compile and run.

### Script files
`ScriptedInput` reads one answer per line:
- a number chooses that option from a staff member's menu; `0` or `WAIT` means Wait
- `Y` / `N` answers the Happy Hour question (Stage 3 only, asked each turn until Happy Hour has been used)
- blank lines and lines starting with `#` are ignored

## Game rules (summary)
- 18 turns, 2 tables, 6 customers arrive (turns 1, 4, 7, 10, 13, 16).
- Each turn: arrivals → manager chooses one task per staff member → eating and payment →
  waiting customers lose satisfaction → customers at 0 satisfaction leave without paying.
- **Victory** needs all three: at least 4 paid customers, revenue of at least $45.00, and average
  satisfaction of paid customers at least 60.

## Stages
| Tag | What it adds | Classes |
|---|---|---|
| `stage-1` | Basic restaurant: Waiter, Chef, Cashier, Regular customers, 3 dishes | 31 |
| `stage-2` | VIP and Critic customers | 33 |
| `stage-3` | Host, combo meals and Happy Hour | 37 |

We worked as if each stage's requirements arrived only after the previous stage was finished,
so each tag contains only that stage's features.

## Diagrams
| Diagram | File |
|---|---|
| Stage 1 class diagram | [Stage1Class.jpg](docs/diagrams/Stage1Class.jpg) |
| Stage 2 class diagram | [Stage2Class.jpg](docs/diagrams/Stage2Class.jpg) |
| Stage 3 class diagram | [Stage3Class.jpg](docs/diagrams/Stage3Class.jpg) |
| Sequence diagram: [use case name] (Stage [2/3]) | [SequenceDiagram.jpg](docs/diagrams/SequenceDiagram.jpg) |

## Change map

Counted with `git diff --name-status`. Interfaces and enums are counted as classes.

| Transition | Added | Modified | Unchanged | Total after |
|---|---|---|---|---|
| Stage 1 → 2 | 2 | 10 | 21 (of 31) | 33 |
| Stage 2 → 3 | 4 | 6 | 27 (of 33) | 37 |

---

## Stage 1 → Stage 2: VIP and Critic customers

Compare: https://github.com/Madhu0071/SC2002-Restaurant-Rushn/compare/stage-1...stage-2

**2 added, 10 modified, 21 unchanged** (including `GameController`, `Restaurant`,
`ObjectiveEvaluator`, `Order` and every staff action).

### What we added
| File | Responsibility |
|---|---|
| `VIPCustomer.java` | Loses 5 satisfaction per waiting turn and gets 10% off the bill (`adjustBill`). |
| `CriticCustomer.java` | Loses 12 per waiting turn, orders the most expensive item (`chooseItem`), and reviews after paying (`onPaid`): +10 if served within 3 turns of arriving, otherwise −10. |

### What we changed, and why
| File | Change |
|---|---|
| `Customer.java` | Added methods that subclasses can override: `adjustBill()`, `chooseItem()`, `onPaid()`. Regular customers use the defaults. |
| `PricingService.java` | Calls the customer's `adjustBill()`, so the VIP discount is applied without checking the customer type. |
| `Cashier.java` | Calls `onPaid()` before recording the payment, so the Critic's review counts in the average. |
| `ArrivalSchedule.java` | The turn 4 arrival is a VIP and the turn 7 arrival is a Critic. |
| `RestaurantCLI.java` / `ScriptedInput.java` | Show each customer's type. |

### Effect on the design
`GameController` was not modified. Each customer's own methods decide its behaviour, so adding a new
customer type needs only a new subclass and one change to `ArrivalSchedule`.

### What still works
- Regular customers still lose 8 per turn and pay full price (seen in both Stage 2 runs).
- Turn order, payment timing and the Victory/Defeat rules are unchanged: `GameController` and
  `ObjectiveEvaluator` were not modified.

---

## Stage 2 → Stage 3: Host, combo meals and Happy Hour

Compare: https://github.com/Madhu0071/SC2002-Restaurant-Rushn/compare/stage-2...stage-3

**4 added, 6 modified, 27 unchanged** (including `Restaurant`, `Menu`, `Order`, every `Customer`
class, every staff action, `Waiter`, `Chef` and `Cashier`).

### What we added
| File | Responsibility |
|---|---|
| `Host.java` | Staff member who seats customers from the queue. A new `Staff` subclass that reuses `SeatCustomerAction`. |
| `ComboMeal.java` | A `MenuItem` made of two items; its price and prep time come from its parts. |
| `PromotionPolicy.java` | Interface for promotions: `adjust()` changes a price and `onTurnStart()` runs each turn. |
| `HappyHourPolicy.java` | One-time Happy Hour for 2 turns: 20% off orders and +15 satisfaction for waiting and seated customers. |

### What we changed, and why
| File | Change |
|---|---|
| `GameController.java` | Registers the Host before the Waiter, adds two combos to the menu, registers Happy Hour with `PricingService`, and adds one step, `startTurnPolicies()`, which asks about Happy Hour and calls `onTurnStart()` on every policy. The other turn steps did not change. |
| `GameSnapshot.java` | Holds the number of Happy Hour turns left, for the display. |
| `PricingService.java` | Keeps a list of `PromotionPolicy` objects (`addPolicy()`) and applies them when pricing an order. |
| `ManagerInput.java` | Added `confirmHappyHour()` so the manager can choose when Happy Hour starts. |
| `RestaurantCLI.java` / `ScriptedInput.java` | Ask for or read the Happy Hour choice and show the turns left. |

### Effect on the design
Each new feature is a new class behind an existing type: the Host is a `Staff`, a combo is a
`MenuItem`, and Happy Hour is a `PromotionPolicy`. The classes that use them work through those types,
so `Restaurant`, `Menu`, `Order` and the Chef did not change. Another promotion could be added by
writing one class and registering it with `addPolicy()`.

### What still works
- The VIP's 10% discount still applies, now on top of Happy Hour: the VIP's Burger cost $10.80.
- The Critic still picks the most expensive item (now a combo) and still gets its +10 review.
- Satisfaction loss is unchanged (Regular −8, VIP −5, Critic −12), seen in the defeat run.
- Turn order, payment timing and the Victory/Defeat rules are unchanged.

---

## Test runs

All runs can be replayed with `java -cp out restaurantrush.Main tests/<file>` on the matching tag.

| Stage | Script | Result |
|---|---|---|
| 1 | `tests/victory.txt` | **VICTORY**: revenue $57.00, average satisfaction 80.00 |
| 1 | `tests/defeat.txt` | **DEFEAT**: every staff member waits, nobody pays |
| 2 | `tests/victory-stage2.txt` | **VICTORY**: revenue $55.50, average satisfaction 81.00 |
| 2 | `tests/defeat-stage2.txt` | **DEFEAT**: customer #1 leaves on turn 13, the Critic on turn 15, nobody pays |
| 3 | `tests/victory-stage3.txt` | **VICTORY**: 5 paid, revenue $86.40, average satisfaction 83.60 |
| 3 | `tests/defeat-stage3.txt` | **DEFEAT**: customer #1 leaves on turn 13, the Critic on turn 15, nobody pays |

Later stages change the menus and prompts, so an older stage's script only replays on its own tag.

### Rules checked in the Stage 3 victory run
- **Happy Hour price:** started on turn 4; the VIP's Burger cost 10.80(15.00 − 20% − 10%).
- **Happy Hour turns left:** showed 2 on turn 4 and 1 on turn 5, then stopped.
- **Critic:** chose the Salad + Pasta combo ($24.30), was served within 3 turns and gained +10.
- **Host:** seated each new customer before the Waiter acted.



