package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.helpers.Team;

/**
 * Player Class.
 */
public class Player {
    private final String name;
    private final Team team;
    private int score = 0;

    /**
     * Constructor, makes a new player.
     * @param name name str.
     * @param team Team object of players team, is either black or white.
     */
    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    /**
     * Get the players name.
     * @return The name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the players team.
     * @return a team object of the players team.
     */
    public Team getTeam() {
        return team;
    }

    public int getScore() {
        return score;
    }

    /**
     * Update the score by a value.
     * @param value The amount to update the score by (can be positive or negative).
     */
    public void updateScore(int value) {
        this.score = score + value;
    }
}
