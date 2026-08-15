# ECS: Elevator Control System

An elevator modelled as a state machine, where the safety rules (doors don't open mid-move,
floor stays in range) are checked in code instead of just hoped for. Basically an excuse to
practice the kind of testing where you check properties instead of individual cases.

> **Status: early development.** State model and two safety invariants are implemented and
> unit tested. Scheduler, tick loop and property-based tests are still to come, see
> [Roadmap](#roadmap).

---

## Why an elevator

Got the idea from [florinpop17/app-ideas](https://github.com/florinpop17/app-ideas), where
it's listed in the advanced tier as an event-handling exercise. I kept the problem but took
it in a different direction.

It's also familiar ground. My [smart home system](https://github.com/pereiravp/POO) from an
OOP course was basically the same shape (discrete state, a clock, actions that change the
world), just without any safety guarantees. Figured I'd revisit it with that piece added in.

---

## Design

Safety properties ("nothing bad ever happens") only need one state or one transition, so
things like "doors stay closed while moving" live in an invariant layer that runs on every
transition. Liveness properties ("every request eventually gets served") need a full run
over time, so those wait for the scheduler.

<details>
<summary>Why atomic ticks, pure invariants, and illegal states that don't compile</summary>

Each transition is one atomic tick and only does one thing: open, close, move a floor, or
idle. Moving from floor 2 to floor 3 with the door open is two transitions, not one, so any
transition that changes floor while a door is open is a violation, no further logic needed.
This also defines the input alphabet for the property-based tests later on.

Invariants are pure predicates, "is this safe?" and nothing else, no side effects, so they
can be tested on their own without spinning up the rest of the system.

`DoorState` is an enum with exactly two members, so an invalid door state can't be
represented at all, the type system rules it out. Same idea with `BuildingConfig(0, 5)` vs
`BuildingConfig(-2, 5)`: whether a negative floor is legal depends on how the building is
configured, not on a hardcoded rule.

</details>

---

## Current state

| Component | Notes |
|---|---|
| `ElevatorState` | Immutable record: floor, direction, door state |
| `BuildingConfig` | Immutable record: min and max floor, both inclusive |
| `Direction`, `DoorState` | Enums; illegal states unrepresentable by construction |
| `doorClosedWhileMoving` | Transition predicate; rejects a floor change while either door is open |
| `floorWithinBounds` | State predicate; rejects a floor outside the configured range |

Nine unit tests, covering accepting and rejecting cases for both invariants and both
inclusive boundaries.

## Roadmap

- [x] State model, both safety invariants, unit tests
- [ ] Property-based testing: random command sequences, no invariant ever violated
- [ ] SCAN scheduling algorithm
- [ ] Tick loop and request handling
- [ ] Liveness check via bounded simulation
- [ ] CI

## Build

Requires JDK 21+ and Maven 3.9+.

```bash
mvn test
```

## Licence

MIT, see [LICENSE](LICENSE).
