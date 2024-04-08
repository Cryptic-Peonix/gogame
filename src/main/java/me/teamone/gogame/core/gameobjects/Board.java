package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;


import java.util.Arrays;
import javafx.scene.layout.GridPane;
import me.teamone.gogame.core.helpers.SpaceState;

/**
 * Class to represent the game board.
 * Is a 2d Array of BoardSpaces.
 */
public class Board {

    private BoardSpace[][] board;
    private final int xSize;
    private final int ySize;

    /**
     * Added by Taran
     *
     * Board's GridPane
     */
    private final GridPane gridPane;


    /**
     * Constructor; creates a board object.
     * @param x The x size of the grid.
     * @param y The y size of the grid.
     */
    public Board(int x, int y) {
        this.xSize = x;
        this.ySize = y;

        //Added by Taran
        //instantiates the board's GridPane
        this.gridPane = new GridPane();

        initBoard();
    }

    /**
     * Generic constructor.
     * Generates a 19x19 Grid.
     */
    public Board() {
        this.xSize = 19;
        this.ySize = 19;

        //Added by Taran
        //instantiates the board's GridPane
        this.gridPane = new GridPane();

        initBoard();
    }

    /**
     * Generate the grid based on defined x and y size
     */
    private void initBoard() {
        this.board = new BoardSpace[this.xSize][this.ySize];
        for (int i = 0; i < this.xSize; i++) {
            for (int j = 0; j < this.ySize; j++) {
                int[] space = {i, j};
                BoardSpace boardSpace = new BoardSpace(space);
                this.board[i][j] = boardSpace;

                //Added by Taran
                //Populates the board's GridPane with BoardSpace StackPanes
                this.gridPane.add(boardSpace.drawBoardSpace(), i, j);
            }
        }
    }

    /**
     * Method to place a stone on the grid.
     * @param stone The stone to place.
     * @param gridPos The space to place the stone on.
     * @throws SpaceFilledException Will throw if the space is already filled or captured
     */
    public void placeStone(Stone stone, int[] gridPos) throws SpaceFilledException, isCapturedException, NoStoneException, StonePlacementException {
        int xPos = gridPos[0];
        int yPos = gridPos[1];
        if (this.getSpecificSpace(xPos, yPos).isCaptured()) {
            throw new isCapturedException("Space at " + xPos + "x" + yPos + " is already captured!");
        }
        if (this.getSpecificSpace(xPos, yPos).getState() == SpaceState.FILLED) {
            throw new SpaceFilledException("Space at " + xPos + "x" + yPos + " is already filled by a " +
                    this.getSpecificSpace(xPos, yPos).getStone().getTeam().toString() + " space!");
        }
        this.getSpecificSpace(xPos, yPos).placeStone(stone);
    }

    /**
     * Get the board
     * @return The board object.
     */
    public BoardSpace[][] getBoard() {
        return board;
    }

    /**
     * Get a specific space on the board based on given x and y pos.
     * @param x The x position.
     * @param y The y position.
     * @return The space at the requested x, y pos.
     * @throws IndexOutOfBoundsException Thrown when trying to get a space that is not within board bounds.
     */
    public BoardSpace getSpecificSpace(int x, int y) throws IndexOutOfBoundsException {
        return this.board[x][y];
    }

    public int getxSize() {
        return this.xSize;
    }

    public int getySize() {
        return ySize;
    }

    /**
     * Print the game board to the console.
     * Used for testing.
     */
    public void printBoard() {
        for (int i = 0; i < this.xSize; i++) {
            System.out.println("Row #" + i + ":" + Arrays.toString(this.board[i]));
        }
    }

    /**
     * Added By Taran
     *
     * Draws the Board
     * @return GridPane the size of the board containing BoardSpace StackPane
     */
    public GridPane drawBoard() { return gridPane; }
}
