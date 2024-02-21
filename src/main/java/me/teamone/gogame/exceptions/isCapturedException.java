package me.teamone.gogame.exceptions;

/**
 * Exception for capture exceptions.
 * Call this whenever an operation on a board space should be illegal because it is captured.
 */
public class isCapturedException extends Exception {

    public isCapturedException(String m) {
        super(m);
    }
}
