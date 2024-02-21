package me.teamone.gogame.gameobjects;

import me.teamone.gogame.exceptions.SpaceFilledException;
import me.teamone.gogame.exceptions.isCapturedException;

import java.util.Arrays;

/**
 * Class to represent the game board.
 * Is a 2d Array of BoardSpaces.
 */
public class Board {

    private BoardSpace[][] board;
    private final int xSize;
    private final int ySize;

    /**
     * Constructor; creates a board object.
     * @param x The x size of the grid.
     * @param y The y size of the grid.
     */
    public Board(int x, int y) {
        this.xSize = x;
        this.ySize = y;
        initBoard();
    }

    /**
     * Generic constructor.
     * Generates a 19x19 Grid.
     */
    public Board() {
        this.xSize = 19;
        this.ySize = 19;
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
                this.board[i][j] = new BoardSpace(space);
            }
        }
    }

    /**
     * Method to place a stone on the grid.
     * @param stone The stone to place.
     * @param gridPos The space to place the stone on.
     * @throws SpaceFilledException Will throw if the space is already filled or captured
     */
    public void placeStone(Stone stone, int[] gridPos) throws SpaceFilledException, isCapturedException {
        //TODO: MAKE ME
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

    /**
     * Print the game board to the console.
     * Used for testing.
     */
    public void printBoard() {
        for (int i = 0; i < this.xSize; i++) {
            System.out.println("Row #" + i + ":" + Arrays.toString(this.board[i]));
        }
    }
}
