package me.teamone.gogame.core.gameobjects;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import me.teamone.gogame.core.Game;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;


import java.util.ArrayList;
import java.util.Arrays;

import me.teamone.gogame.core.helpers.SpaceState;
import me.teamone.gogame.core.helpers.Team;

/**
 * Class to represent the game board.
 * Is a 2d Array of BoardSpaces.
 */
public class Board extends GridPane{

    private BoardSpace[][] board;
    private final int xSize;
    private final int ySize;

    /*
    Added by Taran
    Property to hold the Game the board is apart of
     */
    private final Game game;

    private final String IMAGE_URL = "/images/wood_texture.jpg";

    /**
     * Constructor; creates a board object.
     * @param x The x size of the grid.
     * @param y The y size of the grid.
     */
    public Board(int x, int y, Game game) {
        this.xSize = x;
        this.ySize = y;

        this.game = game;

        initBoard();
    }

    /**
     * Generic constructor.
     * Generates a 19x19 Grid.
     */
    public Board(Game game) {
        this.xSize = 19;
        this.ySize = 19;

        this.game = game;

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
                this.add(boardSpace, i, j);
                BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream(IMAGE_URL), 32, 32, false, false), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);
                boardSpace.setBackground(new Background(myBI));

                boardSpace.setOnMouseClicked(e -> {
                    try {
                        game.playerTurn(boardSpace.getGridSpace());
                    }
                    catch (Exception exc) {
                        exc.printStackTrace();
                    }
                });
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

    /**
     * Checks if a specific space is on the board based on given x and y pos
     * @param x the x position
     * @param y the y postion
     * @return true if the space is in the board, false if it is not
     */
    private boolean isInBoard(int x, int y) {
        return x >= 0 && x < (xSize - 1) && y >= 0 && y < (ySize - 1);
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
     * Get all adjacent spaces horizontally, vertically, and diagonally to the requested space.
     * @param space The space to check.
     * @return An array of the 8 spaces surrounding the requested space.
     */
    public ArrayList<BoardSpace> getAdjacentSpaces(BoardSpace space) {
        ArrayList<BoardSpace> adjacentSpaces = new ArrayList<>();
        //System.out.println(space.toString());
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                try {
                    if (!(x == 0 && y == 0)) {
                        //System.out.println(this.board.getSpecificSpace(space.getX() + x, space.getY() + y));
                        adjacentSpaces.add(getSpecificSpace(space.getX() + x, space.getY() + y));
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        return adjacentSpaces;
    }

    /**
     * Get all adjacent spaces to a GoString
     * @param string the GoString to check
     * @return An array of the spaces surrounding the requested space.
     */
    private ArrayList<BoardSpace> getAdjacentSpaces(GoString string) {
        ArrayList<BoardSpace> adjacentSpaces = new ArrayList<>();
        for (BoardSpace space : string.getSpaces()) {
            int x = space.getX();
            int y = space.getY();
            if (isInBoard(x + 1, y)) {
                adjacentSpaces.add(getSpecificSpace(x + 1, y));
            }
            if (isInBoard(x - 1, y)) {
                adjacentSpaces.add(getSpecificSpace(x - 1, y));
            }
            if (isInBoard(x, y + 1)) {
                adjacentSpaces.add(getSpecificSpace(x, y + 1));
            }
            if (isInBoard(x, y - 1)) {
                adjacentSpaces.add(getSpecificSpace(x, y - 1));
            }
        }
        return adjacentSpaces;
    }

    public void captureSurroundedSpaces(Team team, GoString string) {
        for (BoardSpace space : string.getSpaces()) {
            int x = space.getX() + 1;
            int y = space.getY() + 1;
            // Capture the four adjacent spaces
            captureSurroundedSpace(x + 1, y, team);
            captureSurroundedSpace(x - 1, y, team);
            captureSurroundedSpace(x, y + 1, team);
            captureSurroundedSpace(x, y - 1, team);
        }
    }

    private void captureSurroundedSpace(int x, int y, Team team) {
        // Check if the space is within the board and is empty
        if (x >= 0 && x < xSize && y >= 0 && y < ySize && getSpecificSpace(x, y).isEmpty()) {
            // Change the ownership of the empty space to the specified team
            getSpecificSpace(x, y).captureSpace(team);
            // Recursively capture the connected empty spaces
            captureSurroundedSpace(x + 1, y, team);
            captureSurroundedSpace(x - 1, y, team);
            captureSurroundedSpace(x, y + 1, team);
            captureSurroundedSpace(x, y - 1, team);
        }
    }



}

