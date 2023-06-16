package de.uniks.stpmon.team_m.utils;

public class Position {
    private final int direction;
    private int x;
    private int y;

    public Position(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getDirection() {
        return direction;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
