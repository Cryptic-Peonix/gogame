package me.teamone.gogame.core.gameobjects;

import me.teamone.gogame.core.helpers.Team;

/**
 * Stone object.
 * Used to represent a stone in game.
 */
public class Stone {

    private final Team team;

    public Stone(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
