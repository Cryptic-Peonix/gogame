package me.teamone.gogame.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import me.teamone.gogame.core.exceptions.*;
import me.teamone.gogame.core.gameobjects.*;
import me.teamone.gogame.core.helpers.Team;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Game class. Contains the logic for a game of go.
 */
public class Game {
    private final Player blackPlayer;
    private final Player whitePlayer;
    // string holder hash map
    private final HashMap<Team, ArrayList<GoString>> goStrings = new HashMap<>();

    /*
    Added by Taran
    Property to hold the current turn
     */
    private Player currentPlayer;

    /*
    Added by Taran
    Property to hold the string to display the current player's turn
     */
    private StringProperty currentPlayerStringProperty = new SimpleStringProperty("");

    private final Board board;
    private final int handicapCount;
    /**
     * Counter to adjust stone IDs. moves up 1 by every stone placed
     */
    private int stoneIDcounter;

    /**
     * Constructor, creates a new game instance.
     * @param bp Player 1, this is the black player.
     * @param wp Player 2, this is the white player.
     * @param handicap The amount of handicap stones for the white player, should be 0-9.
     * @param size The size of the board. Example: 19 would use a 19x19 board.
     */
    public Game(Player bp, Player wp, int handicap, int size) {
        this.blackPlayer = bp;
        this.whitePlayer = wp;

        this.currentPlayer = blackPlayer;
        this.currentPlayerStringProperty.set(currentPlayer.getName());

        this.board = new Board(size, size, this);
        this.handicapCount = handicap;
        this.stoneIDcounter = 0;
        // ArrayLists for known strings
        ArrayList<GoString> blackStrings = new ArrayList<>();
        ArrayList<GoString> whiteStrings = new ArrayList<>();
        goStrings.put(Team.BLACK, blackStrings);
        goStrings.put(Team.WHITE, whiteStrings);
    }

    /**
     * Begin the game loop.
     */
    public void gameLoop() {
        // game loop logic here
    }

    /**
     * Method for a player turn in the command line
     *
     * Renamed to playerTurnCmd from playerTurn - Taran
     *
     * @param player The player object to effect.
     */
    public void playerTurnCmd(Player player) throws StonePlacementException, SpaceFilledException, isCapturedException, NoStoneException {
        // get x, and y of location to attempt placement. This is done with cmd for now, must change when gui is created.
        Scanner scan = new Scanner(System.in);
        System.out.println(player.getName() + " please enter the x coordinate of the space you want to place ("
                + this.board.getxSize() + "x" + this.board.getySize() + "):");
        int coordX = scan.nextInt();
        System.out.println(player.getName() + " please enter the y coordinate of the space you want to place ("
                + this.board.getxSize() + "x" + this.board.getySize() + "):");
        int coordY = scan.nextInt();
        // create the stone
        Stone stone = new Stone(player.getTeam(), stoneIDcounter);
        int[] coords = {coordX, coordY};
        // attempt to place stone
        this.board.placeStone(stone, coords);
        // if nothing goes wrong, incriment counter
        this.stoneIDcounter++; // may move to game loop?
        this.board.printBoard();
    }

    /**
     * Method for a player turn
     *
     * @param coords The coordinates to place a stone
     */
    public void playerTurn(int[] coords) throws StonePlacementException, SpaceFilledException, isCapturedException, NoStoneException, mismatchedTeamsException, noStringMatchException, StringCreationException {
        Stone stone = new Stone(this.currentPlayer.getTeam(), stoneIDcounter);
        // attempt to place stone
        this.board.placeStone(stone, coords);
        // if nothing goes wrong, increment counter
        this.stoneIDcounter++; // may move to game loop?

        //Check if we need to add the stone to a string
        BoardSpace space = board.getSpecificSpace(coords[0], coords[1]);

        // see if we can make a new string with the space
        this.attemptStringCreate(space);
        // if we cant make a new one, see if we can add it to an existing one
        if (!space.isInString()) {
            this.attemptAddToString(space);
        }

        if (space.isInString()) {
            for (GoString string : goStrings.get(currentPlayer.getTeam())) {
                if (string.inGoString(space) && string.isLoop()) {
                    board.setCapturesInsideString(string, currentPlayer.getTeam());
                }
            }
        }

        //check if any strings are touching and combine them
        //TODO: Create this method
        this.printGoStrings();



        //TODO: Check board for captures, and remove pieces and invalidate spaces for placement as needed

        //at the end of the turn, switch current players
        switchCurrentPlayer();

        //this.board.printBoard();
    }

    /*
     * Switch the current players
     */
    public void switchCurrentPlayer() {
        currentPlayer = (currentPlayer == blackPlayer) ? whitePlayer : blackPlayer;
        currentPlayerStringProperty.set(currentPlayer.getName());
    }

    /**
     * Get the current player
     */
    public Player getCurrentPlayer() { return currentPlayer; }

    /**
     * Get the current player String
     */
    public StringProperty getCurrentPlayerStringProperty() { return currentPlayerStringProperty; };

    /**
     * Get the current winner of the game.
     * @return The team of the current winner. If the game is tied returns a value of TIE.
     */
    public Team getCurrentWinner() {
        if (this.whitePlayer.getScore() > this.blackPlayer.getScore()) {
            return Team.WHITE;
        } else if (this.whitePlayer.getScore() < this.blackPlayer.getScore()) {
            return Team.BLACK;
        } else {
            return Team.TIE;
        }
    }

    /**
     * Get the game results.
     * @return A hash map of key value pairs with the game results. Results include: winner, player1 score, player2 score.
     */
    public HashMap<String, String> getGameResults() {
        HashMap<String, String> results = new HashMap<>();
        results.put("winner", this.getCurrentWinner().toString());
        results.put("bps", Integer.toString(this.blackPlayer.getScore()));
        results.put("wps", Integer.toString(this.whitePlayer.getScore()));
        return  results;
    }

    /**
     * Get the black player.
     * @return A player object for the black player.
     */
    public Player getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Get the white player.
     * @return A player object for the white player.
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Get the game board
     * @return The game board object.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the handicap count.
     * @return The stone handicap count.
     */
    public int getHandicapCount() {
        return handicapCount;
    }

    /**
     * Check if a handicap exists for the game. Used for scorekeeping.
     * @return True if a handicap exists, false if not.
     */
    public boolean isHandicap() {
        return this.handicapCount > 0;
    }

    public void addGoString(GoString string)  {
        this.goStrings.get(string.getTeam()).add(string);
    }

    public void attemptAddToString(BoardSpace space) throws noStringMatchException, NoStoneException, mismatchedTeamsException {
        if (space.isInString()) {
            throw new RuntimeException("space already exists in string!");
        }
        Team team = space.getStone().getTeam();
        boolean matched = false;
        ArrayList<GoString> strings = this.goStrings.get(team);
        for (GoString s : strings) {
            for (BoardSpace sp : s.getSpaces()) {
                boolean canPlace = GoString.verifySpaces(sp, space);
                if (canPlace) {
                    s.getSpaces().add(space);
                    space.setInString(true);
                    matched = true;
                    break;
                }
                if (matched) {
                    break;
                }
            }
        }
        if (!matched) {
            System.out.println("No String found to add to for stone: " + space.getX() + ", " + space.getY() + "!");
        }
    }

    /**
     * Attempt to create a new string with the space provided. Checks all adjacent spaces and creates one if it can.
     * @param space The space to attempt to make a string with.
     */
    public void attemptStringCreate(BoardSpace space) throws NoStoneException, StringCreationException, mismatchedTeamsException {
        ArrayList<BoardSpace> adjacentSpaces = board.getAdjacentSpaces(space);
        for (BoardSpace sp : adjacentSpaces) {
            if (sp.isInString() || sp.isCaptured()) {
                break;
            }
            if (sp.hasStone()) {
                if (sp.getStone().getTeam() == space.getStone().getTeam()) {
                    GoString string = new GoString(space, sp);
                    this.goStrings.get(space.getStone().getTeam()).add(string);
                    break;
                }
            }
        }
    }



    /**
     * Print all GoStrings on the Board
     */
    public void printGoStrings() {
        ArrayList<GoString> white = this.goStrings.get(Team.WHITE);
        ArrayList<GoString> black = this.goStrings.get(Team.BLACK);

        System.out.println("Black Strings: ");
        for (GoString string : black) {
            System.out.println(string.toString());
        }
        System.out.println("");
        System.out.println("White Strings: ");
        for (GoString stringw : white) {
            System.out.println(stringw.toString());
        }
    }
}
