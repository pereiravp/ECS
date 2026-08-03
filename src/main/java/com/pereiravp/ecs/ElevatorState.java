package com.pereiravp.ecs;

public record ElevatorState(int floor, Direction direction, DoorState door) {
}
