package com.pereiravp.ecs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransitionsTest {

    @Test
    void openDoorOpensDoor() {
        var inicial = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var esperado = new ElevatorState(2, Direction.IDLE, DoorState.OPEN);
        var obtido = Transitions.next(inicial, Action.OPEN_DOOR);

        assertEquals(esperado, obtido, "OPEN_DOOR must set the door to open and leave floor and direction unchanged");
    }

    @Test
    void closeDoorClosesDoor() {
        var inicial = new ElevatorState(2, Direction.IDLE, DoorState.OPEN);
        var esperado = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var obtido = Transitions.next(inicial, Action.CLOSE_DOOR);

        assertEquals(esperado, obtido,
                "CLOSE_DOOR must set the door to close and leave floor and direction unchanged");
    }

    @Test
    void moveUpIncrementsFloor() {
        var inicial = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var esperado = new ElevatorState(3, Direction.UP, DoorState.CLOSED);
        var obtido = Transitions.next(inicial, Action.MOVE_UP);

        assertEquals(esperado, obtido,
                "MOVE_UP must increment the floor, set direction to UP and leave the door unchanged");
    }

    @Test
    void moveDownDecrementsFloor() {
        var inicial = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var esperado = new ElevatorState(1, Direction.DOWN, DoorState.CLOSED);
        var obtido = Transitions.next(inicial, Action.MOVE_DOWN);

        assertEquals(esperado, obtido,
                "MOVE_DOWN must decrement the floor, set direction to DOWN and leave the door unchanged");
    }

    @Test
    void idleLeavesDoorAndFloorUnchanged() {
        var inicial = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var esperado = new ElevatorState(2, Direction.IDLE, DoorState.CLOSED);
        var obtido = Transitions.next(inicial, Action.IDLE);

        assertEquals(esperado, obtido,
                "IDLE must leave the floor, direction and door unchanged");
    }
}
