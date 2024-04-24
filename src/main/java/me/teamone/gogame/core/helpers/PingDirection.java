package me.teamone.gogame.core.helpers;

import java.util.EnumSet;

/**
 * PingDirection enum. Used to define the direction of pings.
 */
public enum PingDirection {
    NORTH(0, 0, 1),
    EAST(1, 1, 0),
    SOUTH(2, 0, -1),
    WEST(3, -1, 0),
    DEFAULT(4, 0, 0);

    private final int id;

    /**
     * The x amount to move during a ping
     */
    private final int moveXAmount;

    /**
     * The y amount to move during a ping
     */
    private final int moveYAmount;

    PingDirection(int id, int x, int y) {
        this.id = id;
        this.moveXAmount = x;
        this.moveYAmount = y;
    }

    public int getMoveXAmount() {
        return moveXAmount;
    }

    public int getMoveYAmount() {
        return moveYAmount;
    }

    public int getId() {
        return id;
    }

    /**
     * Get a direction by its id (0-3). <br>
     * 0 - North. <br>
     * 1 - East. <br>
     * 2 - South. <br>
     * 3 - West.
     * @param id The int of the direction id.
     * @return A PingDirection matching the ID. <br> Will return DEFAULT if the number isn't 0-3
     */
    public static PingDirection getDirectionByID(int id) {
        for (PingDirection d : EnumSet.allOf(PingDirection.class)) {
            if (d.getId() == id) {
                return d;
            }
        }
        return PingDirection.DEFAULT;
    }
}
