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
    void rejectsDoorOpeningWhileMoving(){
        var before = new ElevatorState(2, Direction.UP, DoorState.CLOSED);
        var after = new ElevatorState(3, Direction.UP,DoorState.OPEN);

        assertFalse(inv.doorClosedWhileMoving(before, after),
            "opening the door during movement must be rejected");
    }
}
