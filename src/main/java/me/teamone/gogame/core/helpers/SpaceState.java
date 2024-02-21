package me.teamone.gogame.core.helpers;

/**
 * Enum for representing the state of the board space.
 * Can be open, filled, captured, or doomed.
 * Use these for consistency with logical operations.
 */
public enum SpaceState {
    OPEN,
    FILLED,
    CAPTURED,
    DOOMED
}