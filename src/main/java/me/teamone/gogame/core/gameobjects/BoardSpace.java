package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.NoStoneException;
import me.teamone.gogame.core.Stone;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.helpers.Team;

/**
 * Class for representing a space on the board.
 */
public class BoardSpace {

    /**
     * Boolean used to check if the space contains a stone or not.
     */
    private boolean hasStone;
    /**
     * Container for the space's stone.
     */
    private Stone stone = null;
    /**
     * The spaces position on the grid. Used when board is generated in Board class.
     * Stored as [x, y]
     */
    private final int[] gridSpace;
    /**
     * Boolean used to store if the space is captured or not
     */
    private boolean isCaptured;
    /**
     * Container to determine which team has captured the space.
     */
    private Team captureOwner = null;


    /**
     * Constructor.
     * @param gridSpace The position on the grid.
     */
    public BoardSpace(int[] gridSpace) {
        this.hasStone = false;
        this.isCaptured = false;
        this.gridSpace = gridSpace;
    }

    /**
     * Places a stone on the board.
     * @param stone The stone to place.
     */
    public void placeStone(Stone stone) {
        this.stone = stone;
        this.hasStone = true;
    }

    public void captureSpace(Team team) {
        this.isCaptured = true;
        this.captureOwner = team;
    }

    /**
     * Clears the space, removes the stone.
     */
    public void clearSpace() {
        this.stone = null;
        this.hasStone = false;
    }

    /**
     * Get the space's stone.
     * @return The spaces stone if it exists.
     * @throws NoStoneException Thrown if the space has no stone.
     */
    public Stone getStone() throws NoStoneException {
        if (!hasStone) {
            throw new NoStoneException("Stone missing from grid position: " + gridSpace[0] + " " + gridSpace[1]);
        }
        return stone;
    }

    public Team getCaptureOwner() {
        return captureOwner;
    }

    /**
     * Get the grid space
     * @return The [x, y] array of the grid position.
     */
    public int[] getGridSpace() {
        return gridSpace;
    }

    /**
     * Check if the space is captured.
     * @return True if the space is captured, false if it is not.
     */
    public boolean isCaptured() {
        return isCaptured;
    }

    /**
     * Check if the space is empty
     * @return True if the space has no stone, false if it has a stone.
     */
    public boolean isEmpty() {
        return !hasStone;
    }

}
