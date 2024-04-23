package me.teamone.gogame.core.gameobjects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import me.teamone.gogame.core.helpers.Team;

/**
 * Stone object.
 * Used to represent a stone in game.
 */
public class Stone extends Circle {

    private final Team team;
    /**
     * Int to distinguish stones.
     * Must differentiate so stones for player are not all one reference to the same obj.
     */
    private final int number;

    public Stone(Team team, int number) {
        this.team = team;
        this.number = number;

        this.setRadius(10);
        this.setStroke(Color.BLACK);
        this.setFill(team.getColor());
    }

    public int getNumber() {
        return number;
    }

    public Team getTeam() {
        return team;
    }
}
