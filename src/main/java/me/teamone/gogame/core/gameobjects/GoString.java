package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.mismatchedTeamsException;
import me.teamone.gogame.core.helpers.Team;

import java.util.ArrayList;

/**
 * GoString class. Used to represent a "String" in the game of go.
 */
public class GoString {
    private final ArrayList<BoardSpace> pieces;
    private final Team team;

    public GoString(BoardSpace space1, BoardSpace space2) throws NoStoneException, mismatchedTeamsException {
        if (space1.isEmpty() || space2.isEmpty()) {
            throw new NoStoneException("Spaces must have stones to form a string!");
        }
        if (space1.getStone().getTeam() != space2.getStone().getTeam()) {
            throw new mismatchedTeamsException("The teams of both stones must match to form a string!");
        }
        // set arraylist
        pieces = new ArrayList<>();
        // set team
        this.team = space1.getStone().getTeam();
        // verify poistion
        int space1x = space1.getGridSpace()[0];
        int space1y = space1.getGridSpace()[1];
        int space2x = space2.getGridSpace()[0];
        int space2y = space2.getGridSpace()[1];
        //TODO: add verifcation here
        pieces.add(space1);
        pieces.add(space2);
    }

    public Team getTeam() {
        return team;
    }
}
