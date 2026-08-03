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
}
