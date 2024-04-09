package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.helpers.Team;
import me.teamone.gogame.core.helpers.SpaceState;

import java.util.Objects;

import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class for representing a space on the board.
 */
public class BoardSpace extends StackPane {

    /**
     * Added by Taran
     *
     *Constant for boardspace image url
     */
    private final String IMAGE_URL = "/images/test_boardspace.png";

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
     * Container to determine which team has captured the space.
     */
    private Team captureOwner = null;
    /**
     * The space's state
     */
    private SpaceState state;

    /**
     * Constructor.
     * @param gridSpace The position on the grid.
     */
    public BoardSpace(int[] gridSpace) {
        this.state = SpaceState.OPEN;
        this.gridSpace = gridSpace;

        //add try block
        Image img = new Image(getClass().getResourceAsStream(IMAGE_URL), 43, 43, false, false);

        ImageView imgView = new ImageView(img);
        this.getChildren().add(imgView);
    }

    /**
     * Places a stone on the board.
     * @param stone The stone to place.
     */
    public void placeStone(Stone stone) throws StonePlacementException {
        if (!state.equals(SpaceState.OPEN)) {
            throw new StonePlacementException("Cannot place stone on space x:" + gridSpace[0] + " y:" + gridSpace[1]
                    + "! Space must be empty, is currently: " + state);
        }
        this.stone = stone;
        this.getChildren().add(stone);
        this.state = SpaceState.FILLED;
    }

    /**
     * Capture the space.
     * @param team The team to set the capture to.
     */
    public void captureSpace(Team team) {
        state = SpaceState.CAPTURED;
        captureOwner = team;
        stone = null;
    }

    /**
     * Clears the space, removes the stone.
     */
    public void clearSpace() {
        this.stone = null;
        this.state = SpaceState.OPEN;
    }

    /**
     * Get the space's stone.
     * @return The spaces stone if it exists.
     * @throws NoStoneException Thrown if the space has no stone.
     */
    public Stone getStone() throws NoStoneException {
        if (Objects.isNull(stone)) {
            throw new NoStoneException("Stone missing from grid position: " + gridSpace[0] + " " + gridSpace[1]);
        }
        return stone;
    }

    /**
     * Get the owner of the space if captured.
     * @return The team who captured the space.
     */
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
     * Set the space to be "doomed"
     */
    public void setDoomed() {
        this.state = SpaceState.DOOMED;
    }

    /**
     * Check if the space is captured.
     * @return True if the space is captured, false if it is not.
     */
    public boolean isCaptured() {
        return state.equals(SpaceState.CAPTURED);
    }

    /**
     * Check if the space is empty
     * @return True if the space has no stone, false if it has a stone.
     */
    public boolean isEmpty() {
        return Objects.isNull(stone);
    }

    public SpaceState getState() {
        return this.state;
    }

}
