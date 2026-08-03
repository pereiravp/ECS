# ECS — Elevator Control System

A study project in **safety-critical software design**: modelling an elevator as a state
machine whose safety properties are enforced and verified, rather than assumed.

The goal is not to build a realistic elevator. It is to practise the techniques used in
domains where a wrong state transition is not a bug report — specifying safety properties
explicitly, enforcing them on every transition, and verifying them with property-based
testing instead of hand-picked examples.

> **Status: early development.** The state model and one safety invariant are implemented.
> The scheduler, the remaining invariants and the test suite are not yet written. See
> [Roadmap](#roadmap) for what exists and what does not.

---

## Concepts

### Safety vs. liveness

The properties an elevator must satisfy fall into two categories, and they are verified
in fundamentally different ways:

**Safety properties** — *"nothing bad ever happens"*. Verifiable by inspecting a single
state transition, in isolation:

- The doors are never open while the car is moving
- The car never travels beyond the building's floor range
- A door never opens between floors

**Liveness properties** — *"something good eventually happens"*. Cannot be verified from
a single transition, because "eventually" is not a property of one step:

- Every accepted request is eventually served (no starvation)

Liveness requires bounded simulation: run the system for N ticks and assert that no
pending request outlived some threshold. This distinction drives the design — safety
invariants live in an assertion layer applied to every transition, while liveness will be
checked by a separate simulation harness.

### Design approach

State is modelled with immutable Java records. A transition takes a state and produces a
new one; the invariant layer sits between the two and rejects any transition that would
violate a safety property. This makes the safety properties a checkable artefact of the
codebase rather than a comment.

---

## Current state

Implemented:

| Component | File | Notes |
|---|---|---|
| Elevator state | `ElevatorState` | Immutable record: floor, direction, door state |
| Building bounds | `BuildingConfig` | Immutable record: min and max floor |
| Direction / door enums | `Direction`, `DoorState` | |
| Safety invariant #1 | `SafetyInvariants.doorClosedWhileMoving` | Rejects any transition where the floor changes while a door is open |

**Not implemented:** the remaining invariants, the SCAN scheduler, the tick loop, request
handling, and the test suite. `SafetyInvariantsTest` is currently an empty class — the
build passes with zero tests.

---

## Roadmap

- [x] State model as immutable records
- [x] Safety invariant: doors closed while moving
- [ ] Unit tests for invariant #1
- [ ] Safety invariant: floor always within building bounds
- [ ] Property-based testing — random command sequences, assert no invariant is ever violated
- [ ] SCAN scheduling algorithm
- [ ] Tick loop and request handling
- [ ] Liveness check via bounded simulation
- [ ] CI

---

## Build

Requires JDK 21+ and Maven 3.9+.

```bash
mvn test      # run the test suite
mvn compile   # compile only
```

---

## Licence

MIT — see [LICENSE](LICENSE).
