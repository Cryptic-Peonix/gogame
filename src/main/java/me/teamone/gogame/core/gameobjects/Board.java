package me.teamone.gogame.core.gameobjects;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import me.teamone.gogame.core.Game;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;


import java.util.*;

import me.teamone.gogame.core.helpers.BoardSide;
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

    final int median;

    /**
     * Constructor; creates a board object.
     * @param x The x size of the grid.
     * @param y The y size of the grid.
     */
    public Board(int x, int y, Game game) {
        this.xSize = x;
        this.ySize = y;

        this.game = game;

        this.median = (x / 2 + (1 % 2 + x % 2) / 2) - 1;

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
                BoardSide side = BoardSide.MIDDLE;
                if (i < median) {
                    side = BoardSide.LEFT;
                }
                if (i > median) {
                    side = BoardSide.RIGHT;
                }
                BoardSpace boardSpace = new BoardSpace(space, side);
                this.board[i][j] = boardSpace;

                //Added by Taran
                //Populates the board's GridPane with BoardSpace StackPanes
                this.add(boardSpace, i, j);
                String IMAGE_URL = "/images/wood_texture.jpg";
                BackgroundImage myBI = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_URL)), 32, 32, false, false), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
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
     * Counts the free liberties around a space. IGNORES SPACES THAT DO NOT MATCH TEAM
     * @param space space to check.
     * @param team team to match.
     * @return a count of the liberties.
     */
    public int libertiesFree(BoardSpace space, Team team) {
        ArrayList<BoardSpace> adjSpaces = getAdjacentSpaces(space);
        int counter = 0;
        for (BoardSpace s : adjSpaces) {
            if (GoString.isSpaceDiagnonal(space, s)) {
                try {
                    if (s.getState() == SpaceState.OPEN || s.getStone().getTeam() != team) {
                        counter++;
                    }
                } catch (NoStoneException ignore) {
                }
            }
        }
        return counter;
    }

    /**
     * Checks the free liberties around a space.
     * @param space space to check.
     * @return a count of the liberties
     */
    public int libertiesFree(BoardSpace space) {
        ArrayList<BoardSpace> adjSpaces = getAdjacentSpaces(space);
        int counter = 0;
        for (BoardSpace s : adjSpaces) {
            if (GoString.isSpaceDiagnonal(space, s)) {
                if (s.getState() == SpaceState.OPEN) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Checks if a spaces filled liberties all match the same team. Use in combination with
     * libertiesFree(), this method skips over empty spaces.
     * @param space The BoardSpace to check.
     * @param team The Team to match to.
     * @return true if all liberties are of the same team, false if not.
     */
    public boolean surroundedLibertiesMatchTeam(BoardSpace space, Team team) {
        ArrayList<BoardSpace> adjSpaces = getAdjacentSpaces(space);
        for (BoardSpace s: adjSpaces) {
            if (GoString.isSpaceDiagnonal(space, s)) {
                if (s.getState() != SpaceState.OPEN) { // space either has a stone or is captured
                    if (s.getState() == SpaceState.CAPTURED) {
                        if (s.getCaptureOwner() != team) {
                            return false;
                        }
                    }
                    try {
                        if (s.getState() == SpaceState.FILLED) {
                            if (s.getStone().getTeam() != team) {
                                return false;
                            }
                        }
                    } catch (NoStoneException ignore) {
                    }
                }
            }
        }
        return true;
    }

    /**
     * Get the count of free diagonals adjacent to a space.
     * @param space The BoardSpace to check.
     * @return The count of empty spaces (1-4).
     */
    public int diagonalsFree(BoardSpace space) {
        ArrayList<BoardSpace> adjSpaces = getAdjacentSpaces(space);
        int counter = 0;
        for (BoardSpace s : adjSpaces) {
            if (!GoString.isSpaceDiagnonal(space, s)) {
                if (s.getState() == SpaceState.OPEN) {
                    counter++;
                }
            }
        }
        return counter;
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

    /**
     * Used in capture logic v1 to capture spaces, very buggy, do not use.
     * Sets the captures inside a GoString.
     * @param string The string to capture
     * @param team The team.
     */
    @Deprecated
    public void setCapturesInsideString(GoString string, Team team) {
        List<BoardSpace> coordinatesInside = new ArrayList<>();
        // Find the bounding box of the shape
        Map<String, Integer> boundingBox = string.getBoundingBox();
        int minX = boundingBox.get("minX");
        int minY = boundingBox.get("minY");
        int maxX = boundingBox.get("maxX");
        int maxY = boundingBox.get("maxY");

        // Iterate over all points in the bounding box, ignoring
        //the edges of the bounding box
        for (int x = minX + 1; x <= maxX - 1; x++) {
            for (int y = minY + 1; y <= maxY - 1; y++) {
                System.out.print(x + ", " + y);
                if (isInside(x, y, string, team)) {
                    getSpecificSpace(x, y).captureSpace(team);
                    System.out.println(" is true");
                }
                else {
                    System.out.println();
                }
            }
        }
    }

    /**
     * Checks if a point (x, y) is inside the polygon defined by the outline of a GoString.
     * Uses the ray-casting algorithm to determine if the point is inside the polygon.
     *
     * @param x      The x-coordinate of the point to check.
     * @param y      The y-coordinate of the point to check.
     * @param string The GoString containing the outline of the polygon.
     * @return true if the point is inside the polygon, false otherwise.
     */
    @Deprecated
    public boolean isInside(int x, int y, GoString string, Team team) {
        //if the space is occupied by the same team's stone, it is not inside the string
        if (getSpecificSpace(x, y).getCaptureOwner() != null && getSpecificSpace(x, y).getCaptureOwner().equals(team)) return false;

        // Get the list of BoardSpaces that form the GoString in the same x and y positions as the space
        ArrayList<BoardSpace> xSpaces = new ArrayList<>();
        ArrayList<BoardSpace> ySpaces = new ArrayList<>();

        for(BoardSpace space : string.getSpaces()) {
            //checks every BoardSpace in the string on the same x-axis to the right of given coords
            if (space.getX() == x && space.getY() > y) {
                xSpaces.add(space);
            }
            //checks every BoardSpace in the string on the same y-axis below given coords
            if (space.getY() == y && space.getX() > x) {
                ySpaces.add(space);
            }
        }

        // Sort the xSpaces based on the getY() method
        xSpaces.sort(Comparator.comparingInt(BoardSpace::getY));
        // Sort the ySpaces based on the getX() method
        ySpaces.sort(Comparator.comparingInt(BoardSpace::getX));

        //remove spaces that are in a direct line on the x-axis
        for (int i = xSpaces.size() - 1; i > 0; i--) {
            if ((xSpaces.get(i).getY() - xSpaces.get(i-1).getY()) == 1) {
                //if the difference in Y values between the two spaces is 1
                //then they are next to each other, and one of them needs to be removed.
                xSpaces.remove(i-1);
            }
        }

        //remove spaces that are in a direct line on the y-axis
        for (int i = ySpaces.size() - 1; i > 0; i--) {
            if ((ySpaces.get(i).getX() - ySpaces.get(i-1).getX()) == 1) {
                //if the difference in X values between the two spaces is 1
                //then they are next to each other, and one of them needs to be removed.
                ySpaces.remove(i-1);
            }
        }

        //count how many spaces in arraylists
        int xCount = xSpaces.size();
        int yCount = ySpaces.size();

        System.out.println("xCount" + xCount + " yCount" + yCount);

        //if the count is even, then the space is outside the string
        //if the count is odd, then the space is inside the string
        return xCount % 2 != 0 || yCount % 2 != 0;
    }
}

