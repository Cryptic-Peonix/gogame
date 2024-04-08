package me.teamone.gogame.core;

import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;
import me.teamone.gogame.core.gameobjects.Board;
import me.teamone.gogame.core.gameobjects.Player;
import me.teamone.gogame.core.gameobjects.Stone;
import me.teamone.gogame.core.helpers.Team;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Game class. Contains the logic for a game of go.
 */
public class Game {
    private final Player blackPlayer;
    private final Player whitePlayer;
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
        this.board = new Board(size, size);
        this.handicapCount = handicap;
        this.stoneIDcounter = 0;
    }

    /**
     * Begin the game loop.
     */
    public void gameLoop() {
        // game loop logic here
    }

    /**
     * Method for a player turn
     * @param player The player object to effect.
     */
    public void playerTurn(Player player) throws StonePlacementException, SpaceFilledException, isCapturedException, NoStoneException {
        // get x, and y of location to attempt placement. This is done with cmd for now, must change when gui is created.
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the x coordinate of the space you want to place ("
                + this.board.getxSize() + "x" + this.board.getySize() + "):");
        int coordX = scan.nextInt();
        System.out.println("Please enter the y coordinate of the space you want to place ("
                + this.board.getxSize() + "x" + this.board.getySize() + "):");
        int coordY = scan.nextInt();
        // create the stone
        Stone stone = new Stone(player.getTeam(), stoneIDcounter);
        int[] coords = {coordX, coordY};
        // attempt to place stone
        this.board.placeStone(stone, coords);
        // if nothing goes wrong, incriment counter
        this.stoneIDcounter++; // may move to game loop?
    }

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
}
