package me.teamone.gogame.core.exceptions;

/**
 * Exception for when a board space has no stone.
 */
public class NoStoneException extends Exception {

    public NoStoneException(String message) {
        super(message);
    }
}
