package com.pereiravp.ecs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SafetyInvariantsTest {

    private final SafetyInvariants inv = new SafetyInvariants();

    @Test
    void rejectsMovementWithOpenDoor() {
        var before = new ElevatorState(2, Direction.UP, DoorState.OPEN);
        var after = new ElevatorState(3, Direction.UP, DoorState.OPEN);

        assertFalse(inv.doorClosedWhileMoving(before, after),
                "moving between floors with the door open must be rejected");
    }

    @Test
    void allowsMovementWithClosedDoor() {
        var before = new ElevatorState(2, Direction.UP, DoorState.CLOSED);
        var after = new ElevatorState(3, Direction.UP, DoorState.CLOSED);

        assertTrue(inv.doorClosedWhileMoving(before, after),
                "moving between floors with the door closed must be allowed");
    }

    @Test
    void allowsOpenDoorWhileStationary() {
        var before = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var after = new ElevatorState(2, Direction.IDLE, DoorState.OPEN);

        assertTrue(inv.doorClosedWhileMoving(before, after),
                "opening the door while stationary must be allowed");
    }

    @Test
    void rejectsDoorOpeningWhileMoving() {
        var before = new ElevatorState(2, Direction.UP, DoorState.CLOSED);
        var after = new ElevatorState(3, Direction.UP, DoorState.OPEN);

        assertFalse(inv.doorClosedWhileMoving(before, after),
                "opening the door during movement must be rejected");
    }

    @Test
    void acceptsFloorWithinBounds() {
        var elevator = new ElevatorState(4, Direction.IDLE, DoorState.CLOSED);

        var bounds = new BuildingConfig(0, 5);

        assertTrue(inv.floorWithinBounds(elevator, bounds),
                "a floor inside the building range must be valid");
    }

    @Test
    void acceptsFloorAtUpperBound() {
        var elevator = new ElevatorState(5, Direction.IDLE, DoorState.CLOSED);

        var bounds = new BuildingConfig(0, 5);

        assertTrue(inv.floorWithinBounds(elevator, bounds),
                "the upper bound is inclusive — the top floor is valid");
    }

    @Test
    void rejectsFloorAboveBounds() {
        var elevator = new ElevatorState(6, Direction.IDLE, DoorState.CLOSED);

        var bounds = new BuildingConfig(0, 5);

        assertFalse(inv.floorWithinBounds(elevator, bounds),
                "a floor above the building range must be rejected");
    }

    @Test
    void acceptsFloorAtLowerBound() {
        var elevator = new ElevatorState(0, Direction.IDLE, DoorState.CLOSED);

        var bounds = new BuildingConfig(0, 5);

        assertTrue(inv.floorWithinBounds(elevator, bounds),
                "the lower bound is inclusive - the bottom floor is valid");
    }

    @Test
    void rejectsFloorUnderBounds() {
        var elevator = new ElevatorState(-1, Direction.IDLE, DoorState.CLOSED);

        var bounds = new BuildingConfig(0, 5);

        assertFalse(inv.floorWithinBounds(elevator, bounds),
                "a floor under the building must be rejected");
    }

    @Test
    void acceptsSafeTransition() {
        var before = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var candidate = new ElevatorState(3, Direction.IDLE, DoorState.CLOSED);
        var bounds = new BuildingConfig(0, 5);

        assertTrue(inv.isSafeTransition(before, candidate, bounds),
                "if all safeInvariants are respected it should be accepted");
    }

    @Test
    void rejectsTransitionOutOfBounds() {
        var before = new ElevatorState(5, Direction.IDLE, DoorState.CLOSED);
        var candidate = new ElevatorState(6, Direction.IDLE, DoorState.CLOSED);
        var bounds = new BuildingConfig(0, 5);

        assertFalse(inv.isSafeTransition(before, candidate, bounds),
                "if the safetyInvariants is not respected as it should, the candidate is rejected");
    }
}
