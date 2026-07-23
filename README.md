# Elevator Control Simulator

A discrete simulation of an elevator control system, written in Java and built with Maven. The model enforces safety invariants on every state transition and implements a directional scan policy for scheduling pending floor requests.

Personal project, built on the object-oriented foundations of the Programação Orientada aos Objetos course and extended into areas the coursework did not cover: safety-critical modelling, invariant enforcement, and an automated test suite with Maven and JUnit 5.


## Domain model

| Type | Responsibility |
| --- | --- |
| `Elevador` | A single elevator: current floor, operating state, travel direction and the set of pending requests. |
| `Edificio` | A building holding several elevators; assigns each incoming request to one of them. |
| `Estado` | Operating state: `PARADO`, `A_SUBIR`, `A_DESCER`, `PORTAS_ABERTAS`. |
| `Direcao` | Travel direction used by the scheduler: `SUBIR`, `DESCER`. |

## Design decisions

**States are a closed type, not a string.** `Estado` is an enum, so an elevator cannot hold a value outside the four defined states. The compiler, rather than a runtime check, rules out invalid states.

**Open doors are a state, not a flag.** Modelling doors as a separate boolean would allow the combination "moving" and "doors open" to be represented. Making `PORTAS_ABERTAS` a member of `Estado` removes that combination from the model entirely: an elevator is in exactly one state at a time, so the unsafe pairing cannot be expressed.

**Guards live in the primitive operations.** All validation sits in `subir` and `descer`. `irPara` delegates to them and inherits every guard without duplicating a single condition, so a new safety rule added to the primitives applies to the higher-level command automatically. Range checking is factored into one private predicate used by all callers, keeping the rule in a single place while enforcing it at each public entry point.

**Rejection is silent and total.** An invalid command leaves the elevator untouched instead of throwing. Callers cannot corrupt the model with a bad request, and no caller is required to handle an exception to stay correct.

**Requests are an ordered set.** `TreeSet<Integer>` deduplicates repeated presses of the same button and keeps floors in order, so `higher` and `lower` answer "next stop in this direction" directly, without a separate sorting or search step.

The scheduling policy corresponds to the SCAN algorithm used in real elevator and disk-scheduling systems; it was derived from the problem constraints rather than adopted from a reference implementation.

## Behaviour

**Safety invariants.** An elevator will not move while its doors are open, will not travel outside the configured `[andarMinimo, andarMaximo]` range, and will not accept a movement command in the wrong direction. Every guard is evaluated before any field is written, so a rejected command produces no partial state change.

**Door control.** `abrirPortas` only succeeds from `PARADO`, `fecharPortas` only from `PORTAS_ABERTAS`. Called from any other state, both are no-ops.

**Request scheduling.** `proximoDestino` implements a scan policy: while travelling upward it serves the nearest request above the current floor, and reverses direction only once no request remains above. Requests are not served in arrival order, which avoids the long detours a strict FIFO queue produces. `passo` advances the simulation by one served request.

**Request assignment.** `Edificio.atribuirPedido` assigns an incoming request to the elevator with the smallest absolute distance to the requested floor.

**Encapsulation.** Accessors that expose collections return defensive copies, so external code cannot mutate an elevator's request set or a building's elevator list without passing through the validating methods.

## Known limitations

A movement resolves in a single call rather than one floor per tick, so an elevator cannot pick up a request issued while it is already in transit. The scan policy therefore applies to the requests known at the moment `passo` is called. Removing this restriction is the first item under possible extensions.

## Requirements

- JDK 21 or later
- Maven 3.8 or later

## Build and test

```bash
mvn test
```

To compile and run the entry point:

```bash
mvn compile
java -cp target/classes com.pereira.elevador.Main
```

## Tests

Written with JUnit 5, covering movement, state transitions, boundary validation, safety guards and the scheduling policy:

- Upward and downward movement reach the requested floor
- State returns to `PARADO` after a completed movement
- Movement is refused in both directions while the doors are open, and the state is left unchanged
- A request in the wrong direction leaves the elevator in place
- Floors outside the configured range are rejected, both as movement targets and as requests
- `irPara` dispatches upward, downward, and treats the current floor as a no-op
- A pending request below the current floor is served first when it lies on the way
- Consecutive calls to `passo` serve every pending request
- The scheduler reverses direction when no request remains above the current floor
- A request submitted to a building is registered on one of its elevators

## Project layout

The nested src/main/java and com/pereira/elevador directories are the standard Maven layout and Java package convention respectively; the package name must mirror the directory path.

```
src/main/java/com/pereira/elevador/
    Elevador.java     elevator state, movement and scheduling
    Edificio.java     building and request assignment
    Estado.java       operating states
    Direcao.java      travel directions
    Main.java         entry point
src/test/java/com/pereira/elevador/
    ElevadorTest.java JUnit 5 test suite
```

## Possible extensions (IN DEVELOPMENT)

- Advance travel one floor per simulation tick, allowing requests to be picked up in transit
- Weight and capacity limits as a further safety invariant
- Assign requests by estimated time to serve rather than absolute distance, accounting for each elevator's direction and queue length
