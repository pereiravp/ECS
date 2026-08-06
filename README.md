# ECS — Elevator Control System

A study project in **safety-critical software design**: modelling an elevator as a state
machine whose safety properties are enforced and verified, rather than assumed.

The goal is not to build a realistic elevator. It is to practise the techniques used in
domains where a wrong state transition is not a bug report — specifying safety properties
explicitly, enforcing them on every transition, and verifying them with property-based
testing instead of hand-picked examples.

> **Status: early development.** The state model and two safety invariants are implemented
> and unit tested. The scheduler, the tick loop and the property-based test suite are not
> yet written. See [Roadmap](#roadmap) for what exists and what does not.

---

## Why an elevator

I found the idea in [florinpop17/app-ideas](https://github.com/florinpop17/app-ideas),
where the elevator sits in the advanced tier. The brief there frames it as an exercise in
event handling — occupants call the car from a floor, press buttons inside it, and the
requests have to be queued and serviced in order. The point the author makes is that
elevators were doing event-driven programming long before anyone applied the term to web
applications.

I took the same problem and pointed it somewhere else. What interests me about an elevator
is not the event plumbing but the fact that it is a machine where certain things must
*never* happen. The doors must not be open while the car is moving. The car must not
travel past the top floor. These are not features to be added later — they are the
constraints the rest of the design has to live inside, and there is a whole discipline
built around expressing them precisely and proving they hold.

That discipline is what I actually wanted to practise:

- Separating what the system *decides* from what it must *never do*, so the safety rules
  live in one place instead of being scattered through the logic as `if` statements
- Modelling state so that invalid states cannot be constructed in the first place, rather
  than validating after the fact
- Testing properties instead of examples — generating thousands of random command
  sequences and asserting the rules hold across all of them, rather than hoping I thought
  of the right cases by hand

None of that is taught by writing a UI that moves a box up and down a shaft, and none of
it showed up in my coursework either. It is, however, exactly how software gets built in
railway signalling, avionics, and medical devices — the fields I would like to end up
working in.

There was also a practical reason to pick this particular problem. I had already built a
[smart home management system](https://github.com/pereiravp/POO) for my object-oriented
programming course: devices, rooms, schedules and energy billing over a simulated clock.
An elevator is the same shape of problem — discrete state, a clock, actions that change
the world — but with the safety dimension bolted on top. It let me reuse intuition I had
already paid for while learning something genuinely new, which felt like a better use of
my time than starting from zero on an unfamiliar domain.

---

## Concepts

### Safety vs. liveness

The properties an elevator must satisfy fall into two categories, and they are verified
in fundamentally different ways:

**Safety properties** — *"nothing bad ever happens"*. Verifiable by inspecting a single
state, or a single state transition, in isolation:

- The doors are never open while the car is moving
- The car never travels beyond the building's floor range

**Liveness properties** — *"something good eventually happens"*. Cannot be verified from
a single transition, because "eventually" is not a property of one step:

- Every accepted request is eventually served (no starvation)

Liveness requires bounded simulation: run the system for N ticks and assert that no
pending request outlived some threshold. This distinction drives the design — safety
invariants live in an assertion layer applied to every transition, while liveness will be
checked by a separate simulation harness.

### Modelling assumptions

**A transition is a single atomic tick.** Each transition applies exactly one action from
a closed set — open door, close door, move one floor up, move one floor down, or idle.
No action changes both the car's position and the door state.

This is a deliberate choice, and it has consequences. Going from an open door on floor 2
to floor 3 is not one transition but two: close the door, then move. A transition where
the floor changes and either door state is open is therefore always a violation — either
the system performed two actions in one tick, or it moved with the door open. Both are
rejected.

The alternative model, where a transition spans an interval during which several things
happen, would make this case legal but makes safety properties considerably harder to
verify: the invariant layer would have to reason about orderings within the interval
rather than about a single before/after pair.

This assumption also defines the alphabet used by the property-based tests — random
command sequences are drawn from the closed set of actions above.

### Design approach

State is modelled with immutable Java records. A transition takes a state and produces a
new one; the invariant layer sits between the two and rejects any transition that would
violate a safety property. This makes the safety properties a checkable artefact of the
codebase rather than a comment.

Invariants are pure predicates: they answer *"is this safe?"* and nothing else. They never
mutate state and never decide what the elevator should do — that separation is what makes
them testable without simulating the system.

Where a property can be enforced by the type system, no invariant is written for it. Door
state is an enum with exactly two members, so an invalid door state cannot be constructed
in the first place, and checking for one would be dead code. This is *making illegal
states unrepresentable*, and it is preferred over runtime validation wherever it applies.

Floor bounds are configuration, not a constant. `BuildingConfig(0, 5)` describes a
six-storey building with no basement; `BuildingConfig(-2, 5)` describes the same building
with two basement levels. The invariant checks against the configured range, so negative
floors are only invalid when the building says so.

---

## Current state

Implemented:

| Component | File | Notes |
|---|---|---|
| Elevator state | `ElevatorState` | Immutable record: floor, direction, door state |
| Building bounds | `BuildingConfig` | Immutable record: min and max floor, both inclusive |
| Direction / door enums | `Direction`, `DoorState` | Illegal states unrepresentable by construction |
| Safety invariant #1 | `SafetyInvariants.doorClosedWhileMoving` | Transition predicate — rejects any transition where the floor changes while either door state is open |
| Safety invariant #2 | `SafetyInvariants.floorWithinBounds` | State predicate — rejects any state whose floor falls outside the configured range |

Nine unit tests cover both invariants, including the accepting and rejecting cases for
each and both inclusive boundaries of the floor range.

**Not implemented:** the SCAN scheduler, the tick loop, request handling, property-based
testing, and the liveness check.

---

## Roadmap

- [x] State model as immutable records
- [x] Safety invariant: doors closed while moving
- [x] Unit tests for invariant #1
- [x] Safety invariant: floor always within building bounds
- [x] Unit tests for invariant #2, including boundary cases
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
