package me.teamone.gogame.core.gameobjects;

import javafx.scene.layout.BorderPane;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.StringCreationException;
import me.teamone.gogame.core.exceptions.mismatchedTeamsException;
import me.teamone.gogame.core.helpers.Team;

import java.lang.reflect.Array;
import java.util.*;

/**
 * GoString class. Used to represent a "String" in the game of go.
 */
public class GoString {
    private final ArrayList<BoardSpace> spaces;
    private final Team team;

    public GoString(BoardSpace space1, BoardSpace space2) throws NoStoneException, mismatchedTeamsException, StringCreationException {
        // verify space info
        if (!verifySpaces(space1, space2)) {
            throw new StringCreationException("BoardSpaces requested are not adjacent!");
        }
        // set arraylist
        spaces = new ArrayList<>();
        // set team
        this.team = space1.getStone().getTeam();
        spaces.add(space1);
        space1.setInString(true);
        spaces.add(space2);
        space2.setInString(true);
    }

    /**
     * Checks if a piece, s2, is adjacent (either horizontally, veritcally, or diagonally) to
     * another piece, s1.
     * @param s1 The first boardSpace.
     * @param s2 The second boardSpace.
     * @return True if s2 is adjacent to s1, False if not.
     */
    public static boolean verifySpaces(BoardSpace s1, BoardSpace s2) throws mismatchedTeamsException, NoStoneException {
        if (s1.isEmpty() || s2.isEmpty()) {
            throw new NoStoneException("Spaces must have stones to form a string!");
        }
        if (s1.getStone().getTeam() != s2.getStone().getTeam()) {
            throw new mismatchedTeamsException("The teams of both stones must match to form a string!");
        }
        int s1x = s1.getX();
        int s1y = s1.getY();
        int s2x = s2.getX();
        int s2y = s2.getY();
        // return true if these adjacent conditions are met, false if not.
        return ((s2y == s1y || s2y == s1y + 1 || s2y == s1y - 1) && (s2x == (s1x + 1) || s2x == (s1x - 1))) ||
                ((s2x == s1x || s2x == s1x + 1 || s2x == s1x - 1) && (s2y == (s1y + 1) || s2y == (s1y - 1)));
    }

    /**
     * Add a space to the current string.
     * @param space The space to add.
     * @throws StringCreationException Throws if the requested space is not adjacent to any spaces in the current string.
     * @throws mismatchedTeamsException Throws if the requested space has a stone from rhe wrong team.
     * @throws NoStoneException Throws if the requested space does not have a stone.
     */
    public void addSpace(BoardSpace space) throws StringCreationException, mismatchedTeamsException, NoStoneException {
        boolean valid = false;
        // check if space is adjacent to any board space before adding
        for (BoardSpace sp : this.spaces) {
            if (verifySpaces(sp, space)) {
                valid = true;
                break;
            }
        }
        if (valid) {
            this.spaces.add(space);
            space.setInString(true);
        } else {
            throw new StringCreationException("Boardspace to be added is not adjacent to any spaces!");
        }
    }

    /**
     * Add a list of spaces to the string. used to add more than one space at once.
     * @param spaces The ArrayList of spaces to add.
     * @throws StringCreationException Throws if the requested space is not adjacent to any spaces in the current string.
     * @throws mismatchedTeamsException Throws if the requested space has a stone from rhe wrong team.
     * @throws NoStoneException Throws if the requested space does not have a stone.
     */
    public void addSpaces(ArrayList<BoardSpace> spaces) throws StringCreationException, mismatchedTeamsException, NoStoneException {
        for (BoardSpace s : spaces) {
            addSpace(s);
        }
    }

    /**
     * Checks if the provided space is contained within the GoString.
     * @param space The BoardSpace obj to check.
     * @return true if a space matching the provided spaces x, y exists in the GoString.
     */
    public boolean inGoString(BoardSpace space) {
        boolean found = false;
        for (BoardSpace s : this.spaces) {
            if (s.getX() == space.getX() && s.getY() == space.getY()) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Checks if the provided space is contained within the provided GoString.
     * @param string The GoString to check.
     * @param space The BoardSpace to check.
     * @return true if a space with the matching [x, y] is found within string.
     */
    public static boolean spaceInString(GoString string, BoardSpace space) {
        return string.inGoString(space);
    }

    /**
     * Get the GoString team.
     * @return the Team enum of the GoString.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @return The ArrayList of BoardSpaces that compose the string.
     */
    public ArrayList<BoardSpace> getSpaces() {
        return spaces;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Spaces: ");
        for (BoardSpace s : this.spaces) {
            result.append("(").append(s.getX()).append(", ").append(s.getY()).append("), ");
        }
        result.append(".");
        return result.toString();
    }

    /*public boolean isConnected() {
        BoardSpace firstSpace = spaces.get(0);
        BoardSpace destinationSpace = spaces.get(spaces.size() - 1);
        ArrayList<BoardSpace> visited = new ArrayList<>();

        if (spaces.size() < 4) return false;

        if (isConnected(firstSpace, destinationSpace, firstSpace, visited)) {
            return isConnected(destinationSpace, firstSpace, destinationSpace, visited);
        }

        return false;
    }*/

    /*
    public boolean isConnected(BoardSpace firstSpace, BoardSpace destinationSpace, BoardSpace currentSpace, ArrayList<BoardSpace> visited) {
        if (currentSpace == destinationSpace) {
            return true;
        }

        visited.add(currentSpace);

        for (BoardSpace space : this.getSpaces()) {
            if (space != currentSpace && currentSpace.isAdjacent(space)) {
                if (!visited.contains(space)) {
                    if (isConnected(firstSpace, destinationSpace, space, visited)) {
                        return true;
                    }
                }
            }
        }

        visited.remove(currentSpace);
        return false;
    }*/

    /**
     * Checks if a GoString contains a loop. This is a recursive method
     * as it performs the isLoop check on each stone neighbouring the
     * previous stone the stone is neighbours with a stone that has already
     * been visited.
     * @param currentSpace The current space to check
     * @param firstPriorSpace The space previous to
     * @param visited
     * @return
     */
    public boolean isLoop(BoardSpace currentSpace, BoardSpace firstPriorSpace, BoardSpace secondPriorSpace, ArrayList<BoardSpace> visited) {

        //returns true if current position solves the problem
        if (visited.contains(currentSpace)) {
            return true;
        }

        //this space has now been visited
        visited.add(currentSpace);

        //Else check which options we have at the current position and check if one of the neighbors is a solution
        for (BoardSpace nextSpace : getNeighbours(currentSpace)) {
            System.out.println("nextSpace: " + nextSpace.toString());
            if (!nextSpace.equals(firstPriorSpace) && !nextSpace.equals(secondPriorSpace)) {
                if (isLoop(nextSpace, currentSpace, firstPriorSpace, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * isLoop() function to call without parameters. Automatically starts on the first
     * BoardSpace in the GoString ArrayList
     * @return true if the GoString contains a closed loop, false if it is just a line
     */
    public boolean isLoop() {
        return isLoop(spaces.get(0), null, null, new ArrayList<BoardSpace>());
    }

    /**
     * returns the neighbouring BoardSpaces of a given space
     * @param firstSpace the space to check the neighbours of
     * @return an ArrayList containing the BoardSpace neighbours
     */
    public ArrayList<BoardSpace> getNeighbours(BoardSpace firstSpace) {
        ArrayList<BoardSpace> neighbours = new ArrayList<>();

        for (BoardSpace space : spaces) {
            if (firstSpace != space) {
                try {
                    if (verifySpaces(firstSpace, space)) {
                        neighbours.add(space);
                    }
                } catch (NoStoneException e) {
                    System.out.println("No stone");
                } catch (mismatchedTeamsException e) {
                    System.out.println("Mismatched teams");
                }
            }
        }

        return neighbours;
    }

    public Map<String, Integer> getBoundingBox() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;;
        int maxX = Integer.MIN_VALUE;;
        int maxY = Integer.MIN_VALUE;;

        for (BoardSpace space : spaces) {
            if (space.getX() < minX) minX = space.getX();
            if (space.getX() > maxX) maxX = space.getX();
            if (space.getY() < minY) minY = space.getY();
            if (space.getY() > maxY) maxY = space.getY();
        }

        Map<String, Integer> boundingBox = new HashMap<>();
        boundingBox.put("minX", minX);
        boundingBox.put("minY", minY);
        boundingBox.put("maxX", maxX);
        boundingBox.put("maxY", maxY);

        return boundingBox;

    }

}
