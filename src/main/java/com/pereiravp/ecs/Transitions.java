package com.pereiravp.ecs;

public class Transitions {

    private Transitions() {
    }

    public static ElevatorState next(ElevatorState state, Action action) {
        return switch (action) {
            case OPEN_DOOR -> new ElevatorState(state.floor(), state.direction(), DoorState.OPEN);
            case CLOSE_DOOR -> new ElevatorState(state.floor(), state.direction(), DoorState.CLOSED);
            case MOVE_UP -> new ElevatorState(state.floor() + 1, Direction.UP, state.door());
            case MOVE_DOWN -> new ElevatorState(state.floor() - 1, Direction.DOWN, state.door());
            case IDLE -> new ElevatorState(state.floor(), Direction.IDLE, state.door());
        };
    }
}
