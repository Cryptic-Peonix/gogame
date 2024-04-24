package me.teamone.gogame.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import me.teamone.gogame.core.exceptions.*;
import me.teamone.gogame.core.gameobjects.*;
import me.teamone.gogame.core.helpers.BoardSide;
import me.teamone.gogame.core.helpers.PingDirection;
import me.teamone.gogame.core.helpers.SpaceState;
import me.teamone.gogame.core.helpers.Team;

import java.util.*;

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
    private final StringProperty currentPlayerStringProperty = new SimpleStringProperty("");

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
        // check if this space bridges one or more strings
        ArrayList<Integer> uniqueAdjacentGoStringIndexes = getUniqueAdjacentStringIndexes(space);
        if (uniqueAdjacentGoStringIndexes.size() > 1) {
            // bypass checking because we already did it
            space.setInString(true);
            this.goStrings.get(this.currentPlayer.getTeam()).get(uniqueAdjacentGoStringIndexes.get(0)).addSpace(space);
            attemptStringMerge(uniqueAdjacentGoStringIndexes, this.currentPlayer.getTeam());
        }
        // if we cant make a new one, see if we can add it to an existing one
        if (!space.isInString()) {
            this.attemptAddToString(space);
        }
        for (GoString string : this.goStrings.get(this.currentPlayer.getTeam())) {
            stringCleanup(string);
            captureChecker(string);
        }

        // This is an easy way of doing it but we gotta make it work
        // check every single space on the board and set capture if surrounded by enemy team
        for (int i = 0; i < board.getxSize(); i++) {
            for (int j = 0; j < board.getySize(); j++) {
                BoardSpace s = board.getSpecificSpace(i, j);
                if (s.getState() != SpaceState.CAPTURED && board.libertiesFree(s) == 0 && board.surroundedLibertiesMatchTeam(s, this.currentPlayer.getTeam())) {
                    s.captureSpace(this.currentPlayer.getTeam());
                }
            }
        }

        System.out.println(space.getSide());
        this.printGoStrings();

        //calculate scores

        //at the end of the turn, switch current players
        switchCurrentPlayer();
        System.out.println();
    }

    public ArrayList<Integer> getUniqueAdjacentStringIndexes(BoardSpace space) throws NoStoneException, mismatchedTeamsException {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (GoString str : this.goStrings.get(space.getStone().getTeam())) {
            for (BoardSpace s : str.getSpaces()) {
                if (GoString.verifySpaces(space, s)) {
                    indexes.add(this.goStrings.get(space.getStone().getTeam()).indexOf(str));
                    break;
                }
            }
        }
        return indexes;
    }

    public void attemptStringMerge(ArrayList<Integer> stringIndexes, Team team) {
        ArrayList<GoString> strings = new ArrayList<>();
        for (int i : stringIndexes) {
            GoString string = this.goStrings.get(team).get(i);
            strings.add(string);
        }
        if (strings.size() == 2) {
            strings.get(0).consume(strings.get(1));
            this.goStrings.get(team).remove(strings.get(1));
        } else {
            strings.get(0).consume(strings.get(1));
            this.goStrings.get(team).remove(strings.get(1));
            stringIndexes.remove(1);
            for (int j = 1; j < stringIndexes.size(); j++) {
                stringIndexes.set(j, stringIndexes.get(j) - 1);
            }
            attemptStringMerge(stringIndexes, team); // recursive until we get down to 2
        }
    }

    /**
     * Attempt to "cleanup" a string. Check all the spaces surrounding a string and absorb any
     * spaces with stones of the same team that aren't in a string.
     * @param string the GoString to check
     */
    private void stringCleanup(GoString string) {
        try {
            ArrayList<BoardSpace> spacesToAdd = new ArrayList<>();
            for (BoardSpace space : string.getSpaces()) {
                for (BoardSpace aSpace : board.getAdjacentSpaces(space)) { //get adjacent spaces
                    if (aSpace.getState() == SpaceState.FILLED && aSpace.getStone().getTeam() == string.getTeam()
                            && !aSpace.isInString()) {
                        spacesToAdd.add(aSpace); // cant edit spaces in string while iterating.
                    }
                }
            }
            spacesToAdd.forEach(e -> {
                string.getSpaces().add(e); // bypass checks because we have already verified above
                e.setInString(true);
            });
        } catch (NoStoneException ignore) { // ignore for now
        }
    }

    public void captureChecker (GoString string) {
        ArrayList<int[]> captureCannidites = new ArrayList<>();
        for (BoardSpace space : string.getSpaces()) {
            for (int pingsPerformed = 0; pingsPerformed < 4; pingsPerformed++) {
                //TODO: fix some capture errors
                performPing(string, space, PingDirection.getDirectionByID(pingsPerformed)).forEach(e -> {
                    boolean addable = true;
                    for (int[] c : captureCannidites) {
                        if (c[0] == e[0] && c[1] == e[1]) {
                            addable = false;
                            break;
                        }
                    }
                    if (addable) {
                        captureCannidites.add(e);
                    }
                });
            }
        }
        if (!captureCannidites.isEmpty()) {
            attemptCaptureSpread(captureCannidites, string.getTeam());
        }
    }

    private void attemptCaptureSpread(ArrayList<int[]> cannidates, Team team) {
        boolean surrounded = false; //assume invalid
        GoString cannidateString = new GoString(team); //gen empty string;
        cannidates.forEach(e -> { //fill string
           cannidateString.getSpaces().add(board.getSpecificSpace(e[0], e[1]));
        });
        Map<String, Integer> boundingBox = cannidateString.getBoundingBox();
        System.out.println("cannidates: " + cannidateString);
        System.out.println(boundingBox.toString());
        int minX = boundingBox.get("minX");
        int minY = boundingBox.get("minY");
        int maxX = boundingBox.get("maxX");
        int maxY = boundingBox.get("maxY");
        if (minX == maxX && minY == maxY) { //box is one square
            if (board.libertiesFree(board.getSpecificSpace(minX, minY)) == 0 && board.surroundedLibertiesMatchTeam(board.getSpecificSpace(minX, minY), team)) {
                surrounded = true;
            }
        } else if (minX == maxX) { // box is a vertical line
            System.out.println("Vertical line Spanning from: " + minY + " - " + maxY + " on row: " + minX + ".");
            boolean allValid = true;
            for (int y = minY; y <= maxY; y++) {
                if (y == minY || y == maxY) { // if we are on and end of the line
                    if (!(board.libertiesFree(board.getSpecificSpace(minX, y), team) <= 1)) {
                        allValid = false;
                        break;
                    }
                } else { // if we are in the middle of a line
                    if (!(board.libertiesFree(board.getSpecificSpace(minX, y), team) <= 2)) { // stones in the middle have 2 free liberties
                        allValid = false;
                        break;
                    }
                }
            }
            if (allValid) {
                surrounded = true;
            }
        } else if (minY == maxY) { // horizontal line
            boolean allValid = true;
            for (int x = minX; x <= maxX; x++) {
                if (x == minX || x == maxX) {
                    if (!(board.libertiesFree(board.getSpecificSpace(x, minY), team) <= 1)) {
                        allValid = false;
                        break;
                    }
                } else {
                    if (!(board.libertiesFree(board.getSpecificSpace(x, minY), team) <= 2)) {
                        allValid = false;
                        break;
                    }
                }
            }
            if (allValid) {
                surrounded = true;
            }
        } else { // we have more complex geometry, could be a box, could be some weird unconnected geometry, etc
            boolean allValid = true;
            // loop through all spaces in the bounding box
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    // check if the space is in our list of cannidates
                    if (cannidateString.inGoString(board.getSpecificSpace(x, y))) {
                        // check if the space is an edge
                        if (x == minX || x == maxX || y == minY || y == maxY) {
                            // check if the space is a corner
                            if (x == minX && y == minY || x == minX && y == maxY || x == maxX && y == minY || x == maxX && y == maxY) {
                                System.out.println(x + ", " + y + " checking corner here.");
                                if (board.libertiesFree(board.getSpecificSpace(x,y), team) > 2) {
                                    allValid = false;
                                }
                            } else { //if it's not a corner it's an edge
                                System.out.println(x + ", " + y + " checking edge here.");
                                if (board.libertiesFree(board.getSpecificSpace(x,y), team) > 3) {
                                    allValid = false;
                                }
                            }
                        }
                    }
                }
                if (!allValid) {
                    break;
                }
            }
            if (allValid) {
                surrounded = true;
            }
        }
        // check the bounding box for proper surrounded
        if (surrounded && verifyBoundingBox(minX, maxX, minY, maxY, team)) {
            cannidates.forEach(e -> { //capture
                board.getSpecificSpace(e[0], e[1]).captureSpace(team);
                if (team.equals(Team.WHITE)) {
                    whitePlayer.updateScore(1);
                }
                else if (team.equals(Team.BLACK)) {
                    blackPlayer.updateScore(1);
                }
            });
            System.out.println("surrounded");
        } else {
            System.out.println("not surrounded");
        }
    }

    /**
     * Verify a bounding box to ensure all pieces bordering it are of the same team
     * @param minX min x of box.
     * @param maxX max x of box.
     * @param minY min y of box.
     * @param maxY max y of box.
     * @param team team to check.
     * @return True if all surrounding pieces are of the matching team, false if not.
     */
    private boolean verifyBoundingBox(int minX, int maxX, int minY, int maxY, Team team) {
        try {
            // bottom & top of box
            for (int i = minX; i <= maxX; i++) {
                // bottom
                if (board.getSpecificSpace(i, minY - 1).getStone().getTeam() != team) {
                    return false;
                }
                // top
                if(board.getSpecificSpace(i, maxY + 1).getStone().getTeam() != team) {
                    return  false;
                }
            }
            // left and right
            for (int j = minY; j <= maxY; j++) {
                // left
                if (board.getSpecificSpace(minX - 1, j).getStone().getTeam() != team) {
                    return  false;
                }
                //right
                if (board.getSpecificSpace(maxX + 1, j).getStone().getTeam() != team) {
                    return false;
                }
            }
        } catch (NoStoneException | ArrayIndexOutOfBoundsException ignore) {
        }
        return true;
    }

    private ArrayList<int[]> performPing(GoString string, BoardSpace space, PingDirection direction) {
        ArrayList<BoardSpace> foundSpaces = new ArrayList<>();
        ArrayList<int[]> foundCoords = new ArrayList<>();
        int xMove = direction.getMoveXAmount();
        int yMove = direction.getMoveYAmount();
        final int MAX_TURNS = 1;
        int total_turns = 0;
        boolean doCapture = false;
        BoardSpace lastSpace = space;
        while (total_turns <= MAX_TURNS) {
            BoardSpace nextSpace;
            try {
                nextSpace = board.getSpecificSpace(lastSpace.getX() + xMove, lastSpace.getY() + yMove);
            } catch (ArrayIndexOutOfBoundsException e) { // if we hit an edge we need to make a turn
                if (lastSpace.getSide() == BoardSide.LEFT || lastSpace.getSide() == BoardSide.MIDDLE) {
                    if (direction == PingDirection.NORTH || direction == PingDirection.SOUTH) {
                        xMove = PingDirection.EAST.getMoveXAmount();
                        yMove = PingDirection.EAST.getMoveYAmount();
                    } else {
                        xMove = PingDirection.SOUTH.getMoveXAmount();
                        yMove = PingDirection.SOUTH.getMoveYAmount();
                    }
                } else {
                    if(direction == PingDirection.NORTH || direction == PingDirection.SOUTH) {
                        xMove = PingDirection.WEST.getMoveXAmount();
                        yMove = PingDirection.WEST.getMoveYAmount();
                    } else {
                        xMove = PingDirection.NORTH.getMoveXAmount();
                        yMove = PingDirection.NORTH.getMoveYAmount();
                    }
                }
                total_turns++;
                continue;
            }
            // check if the next space we hit is a space with a stone in our string
            if (nextSpace.hasStone()) {
                if (string.inGoString(nextSpace)) {
                    doCapture = true;
                    break; // we have returned to our stone
                }
                else {
                    try {
                        if (nextSpace.getStone().getTeam() != string.getTeam()) {
                            foundSpaces.add(nextSpace);
                        }
                    }
                    catch (NoStoneException e)  {
                        System.out.println("No stone in space");
                    }
                }
            }
            // check if we can add the next space to the capture list
            if (nextSpace.getState() == SpaceState.OPEN) {
                foundSpaces.add(nextSpace);
            }
            lastSpace = nextSpace;
        }
        if (doCapture) {
            foundSpaces.forEach(e -> {
                foundCoords.add(new int[]{e.getX(), e.getY()});
            });
        }
        return foundCoords;
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
        System.out.println();
        System.out.println("White Strings: ");
        for (GoString stringw : white) {
            System.out.println(stringw.toString());
        }
    }
}
