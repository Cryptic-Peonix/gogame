package me.teamone.gogame.exceptions;

/**
 * Error to be called when a space is filled.
 */
public class SpaceFilledException extends Exception {

    public SpaceFilledException(String m) {
        super(m);
    }
}
