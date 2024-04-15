package me.teamone.gogame.core.exceptions;

/**
 * Exception for mismatched teams.
 * Call whenever two objects teams are meant to be the same, but they aren't.
 */
public class mismatchedTeamsException extends Exception {

    public mismatchedTeamsException(String m) {
        super(m);
    }
}
