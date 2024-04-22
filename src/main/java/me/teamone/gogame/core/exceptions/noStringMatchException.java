package me.teamone.gogame.core.exceptions;

/**
 * Exception for capture exceptions.
 * Call this whenever an operation on a board space should be illegal because it is captured.
 */
public class noStringMatchException extends Exception {

    public noStringMatchException(String m) {
        super(m);
    }
}
