# ECS — Elevator Control System

Modelling an elevator as a state machine whose safety properties are **enforced and
verified, not assumed**. A study in the techniques used where a wrong state transition
is not a bug report.

> **Status: early development.** State model and two safety invariants implemented and
> unit tested. Scheduler, tick loop and property-based tests still to come — see
> [Roadmap](#roadmap).

---

## Why an elevator

The idea comes from [florinpop17/app-ideas](https://github.com/florinpop17/app-ideas),
where it sits in the advanced tier as an exercise in event handling. I took the same
problem and pointed it elsewhere.

It also builds on ground I'd already covered. My [smart home system](https://github.com/pereiravp/POO)
for an OOP course was the same shape of problem — discrete state, a clock, actions that
change the world — with the safety dimension missing. Same intuition, new discipline.

---

## Design

**Safety vs. liveness.** Safety properties (*"nothing bad ever happens"*) are checkable
from a single state or transition — doors closed while moving, floor within bounds. These
live in an invariant layer applied to every transition. Liveness properties (*"every
request is eventually served"*) can't be checked from one step and need bounded
simulation, so they're handled separately.

**A transition is one atomic tick.** Exactly one action per transition — open, close, move
one floor, or idle. No action changes both position and door state. So going from an open
door on floor 2 to floor 3 isn't one transition but two, and any transition where the
floor changes with a door open is always a violation. The alternative — a transition
spanning an interval — would make that case legal but forces the invariant layer to reason
about orderings instead of a single before/after pair. This also defines the alphabet for
the property-based tests.

**Invariants are pure predicates.** They answer *"is this safe?"* and nothing else — no
mutation, no decisions. That's what makes them testable without simulating the system.

**Type system first.** `DoorState` is an enum with two members, so an invalid door state
can't be constructed and checking for one would be dead code. Illegal states are made
unrepresentable rather than validated after the fact.

**Bounds are configuration.** `BuildingConfig(0, 5)` is a six-storey building;
`BuildingConfig(-2, 5)` adds two basement levels. Negative floors are only invalid when
the building says so.

---

## Current state

| Component | Notes |
|---|---|
| `ElevatorState` | Immutable record: floor, direction, door state |
| `BuildingConfig` | Immutable record: min and max floor, both inclusive |
| `Direction`, `DoorState` | Enums — illegal states unrepresentable by construction |
| `doorClosedWhileMoving` | Transition predicate — rejects a floor change while either door is open |
| `floorWithinBounds` | State predicate — rejects a floor outside the configured range |

Nine unit tests, covering accepting and rejecting cases for both invariants and both
inclusive boundaries.

## Roadmap

- [x] State model, both safety invariants, unit tests
- [ ] Property-based testing — random command sequences, no invariant ever violated
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

MIT — see [LICENSE](LICENSE).
