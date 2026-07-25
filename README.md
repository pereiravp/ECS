# Elevator Control Simulator (ECS)

A discrete simulation of a single-elevator control system in Java,
built around one idea: some states must never be reachable.

> Personal project — not coursework. Built to explore safety-critical
> control logic, scheduling and test-driven design.

## Motivation

An elevator looks trivial and isn't. Deciding the order in which to
serve requests is a real scheduling problem (the SCAN / "elevator"
algorithm). More importantly, an elevator is a *safety-critical*
system: certain conditions must hold on **every** state transition,
no matter what commands arrive. This project models both.

## Safety invariants (enforced on every transition)

- The car never moves while the door is open.
- The door never opens between floors.
- The car never travels beyond the lowest or highest floor.
- Every accepted request is eventually served (no starvation).

Unsafe commands are refused, not silently ignored.

## Core features (v1)

- Discrete, tick-based simulation of car state (floor, direction, door).
- SCAN scheduling: serve requests in the current direction before
  reversing.
- Command validation layer that rejects unsafe inputs.
- JUnit 5 test suite covering movement, boundary conditions and
  refusal of unsafe commands.

## Design overview

State is immutable per tick: each transition produces a new
`ElevatorState` rather than mutating in place, making invariants easy
to assert and behaviour easy to test.

## Running

```bash
mvn compile
mvn test
```

## Roadmap

- [ ] v1: single elevator, SCAN, invariants, JUnit suite
- [ ] Request generation / simple simulation driver
- [ ] Metrics: average wait time, total travel distance
- [ ] (future) Concurrency: multiple cars, thread-safe dispatch
- [ ] (future) Security layer — separate follow-up project

## Author

Gonçalo Simões Pereira — BSc Computer Science, University of Minho
