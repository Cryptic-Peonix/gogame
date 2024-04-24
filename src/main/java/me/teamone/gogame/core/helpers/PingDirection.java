package me.teamone.gogame.core.helpers;

import java.util.EnumSet;

public enum PingDirection {
    NORTH(0, 0, 1),
    EAST(1, 1, 0),
    SOUTH(2, 0, -1),
    WEST(3, -1, 0),
    DEFAULT(4, 0, 0);

    private final int id;
    private final int moveXAmount;
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

    public static PingDirection getDirectionByID(int id) {
        for (PingDirection d : EnumSet.allOf(PingDirection.class)) {
            if (d.getId() == id) {
                return d;
            }
        }
        return PingDirection.DEFAULT;
    }
}
