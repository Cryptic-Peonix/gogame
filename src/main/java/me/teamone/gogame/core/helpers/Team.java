package me.teamone.gogame.core.helpers;

import javafx.scene.paint.Color;

/**
 * Enum for teams.
 *
 * Used to keep consistency when comparing teams, and to easily store any hardcoded team data.
 */
public enum Team {
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    TIE(Color.BEIGE);

    /**
     * The color used to represent the team.
     */
    private final Color color;

    /**
     * Team constructor
     * @param color the color to make the team.
     */
    Team(Color color) {
        this.color = color;
    }

    /**
     * Get the selected teams color.
     * @return The color of the team.
     */
    public Color getColor() {
        return color;
    }
}
