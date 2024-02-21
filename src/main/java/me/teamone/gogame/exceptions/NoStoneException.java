package me.teamone.gogame.exceptions;

/**
 * Exception for when a board space has no stone.
 */
public class NoStoneException extends Exception {

    public NoStoneException(String message) {
        super(message);
    }
}
