package com.pereiravp.ecs;

public class SafetyInvariants {

    public boolean doorClosedWhileMoving(ElevatorState before, ElevatorState after) {

        boolean moved = before.floor() != after.floor();
        boolean doorOpen = before.door() == DoorState.OPEN
                || after.door() == DoorState.OPEN;

        return !(moved && doorOpen);
    }

    public boolean floorWithinBounds(ElevatorState state, BuildingConfig config) {
        return state.floor() >= config.minFloor()
                && state.floor() <= config.maxFloor();
    }
}
