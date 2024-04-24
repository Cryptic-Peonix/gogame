package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.StringCreationException;
import me.teamone.gogame.core.exceptions.mismatchedTeamsException;
import me.teamone.gogame.core.helpers.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public GoString(Team team) {
        this.team = team;
        this.spaces = new ArrayList<>();
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

    /**
     * Adds the requested goStrings pieces to this one. DOES NOT PERFORM VERIFICATION CHECKS, CALL ONLY WHEN YOU KNOW
     * THEY BORDER. Does not delete the food string, you must call that yourself by setting it to null.
     * @param food The GoString to eat.
     */
    public void consume(GoString food) {
        this.spaces.addAll(food.getSpaces());
    }

    public void wipeSpaces() {
        this.spaces.clear();
    }

    public boolean isEmpty() {
        return this.spaces.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Spaces: ");
        for (BoardSpace s : this.spaces) {
            result.append("(").append(s.getX()).append(", ").append(s.getY()).append("), ");
        }
        //result.append("\n isLoop: ").append(isLoopV2());
        return result.toString();
    }

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

    public boolean isLoopV2() {
        ArrayList<BoardSpace> previousSpaces = new ArrayList<>();
        Random random = new Random();
        // get random space in string
        BoardSpace seed = this.spaces.get(this.spaces.size() - 1); // get last stone added to GoString
        System.out.println(this.team + " seed: " + seed);
        BoardSpace currentSpace = seed;
        while (true) {
            previousSpaces.add(currentSpace);
            attemptPrevTrim(previousSpaces); // trim previous spaces to maintain a size of 2
            ArrayList<BoardSpace> neighbors = getNeighbours(currentSpace);
            System.out.println("N: " + neighbors);
            if (neighbors.isEmpty()) {
                System.out.println("not enough neighbors surrounding: " + currentSpace);
                return false; // early escape, we have hit an edge, so it is def not a loop
            }
            ArrayList<BoardSpace> nonDiagNeighbors = getNonDiagSpaces(currentSpace, neighbors);
            System.out.println("NdN: " + nonDiagNeighbors);
            BoardSpace nextSpace = null;
            boolean allNonDiagInvalid = true;
            if (!nonDiagNeighbors.isEmpty()) { // if all of our neighbors are horizontal and vertical
                for (BoardSpace s : nonDiagNeighbors) {
                    if (!spaceInPrevious(previousSpaces, s)) {
                        nextSpace = s;
                        allNonDiagInvalid = false;
                        break;
                    }
                }
                if (allNonDiagInvalid) {
                    for (BoardSpace s : neighbors) {
                        if(!spaceInPrevious(previousSpaces, s)) {
                            nextSpace = s;
                            break;
                        }
                    }
                }
            } else {
                for (BoardSpace s : neighbors) {
                    if(!spaceInPrevious(previousSpaces, s)) {
                        nextSpace = s;
                        break;
                    }
                }
            }
            if (nextSpace == null) {
                System.out.println("No valid space found after: " + currentSpace);
                return false; // couldn't find a valid next space, not a loop
            }
            System.out.println(nextSpace);
            System.out.println("P: " + previousSpaces);
            // check if we have hit the seed again
            if (nextSpace.getX() == seed.getX() && nextSpace.getY() == seed.getY()) {
                System.out.println("its a loop!");
                break; // we have connected
            }
            currentSpace = nextSpace;
            System.out.println("continuing...");
        }
        return true;
    }

    private boolean spaceInPrevious(ArrayList<BoardSpace> spaces, BoardSpace s) {
        for (BoardSpace sp : spaces) {
            if (sp.getY() == s.getY() && sp.getX() == s.getX()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<BoardSpace> getNonDiagSpaces(BoardSpace origin, ArrayList<BoardSpace> spaces) {
        ArrayList<BoardSpace> nonDiag = new ArrayList<>();
        for (BoardSpace space : spaces) {
            if (isSpaceDiagnonal(origin, space)) {
                nonDiag.add(space);
            }
        }
        return  nonDiag;
    }

    /**
     * Checks if an adjacent space is diagonal. WILL RETURN FALSE IF THE SPACES ARE NOT ADJACENT.
     * @param origin The origin point to check.
     * @param check The second point to check.
     * @return True if it is a diagonal, false if horizontal/vertical.
     */
    public static boolean isSpaceDiagnonal(BoardSpace origin, BoardSpace check) {
        return (origin.getX() + 1 != check.getX() || origin.getY() + 1 != check.getY()) &&
                (origin.getX() - 1 != check.getX() || origin.getY() + 1 != check.getY()) &&
                (origin.getX() + 1 != check.getX() || origin.getY() - 1 != check.getY()) &&
                (origin.getX() - 1 != check.getX() || origin.getY() - 1 != check.getY());
    }

    private void attemptPrevTrim(ArrayList<?> s) {
        if (s.size() > 2) {
            s.remove(0);
        }
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
