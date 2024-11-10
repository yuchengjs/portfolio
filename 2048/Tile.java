package org.cis1200.game2048;

import java.awt.*;
import java.util.Objects;

public class Tile {
    private int number;
    private Color color;

    public Tile() {
        this.number = 0;
    }

    public Tile(int number) {
        this.number = number;
        setColor();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        setColor();
    }

    public Color getColor() {
        return color;
    }

    public void setColor() {
        if (number == 2) {
            this.color = new Color(255, 222, 251);
        } else if (number == 4) {
            this.color = new Color(228, 237, 197);
        } else if (number == 8) {
            this.color = new Color(197, 234, 237);
        } else if (number == 16) {
            this.color = new Color(225, 197, 237);
        } else if (number == 32) {
            this.color = new Color(255, 201, 201);
        } else if (number == 64) {
            this.color = new Color(153, 225, 196);
        } else if (number == 128) {
            this.color = new Color(153, 153, 225);
        } else if (number == 256) {
            this.color = new Color(225, 153, 204);
        } else if (number == 512) {
            this.color = new Color(153, 204, 255);
        } else if (number == 1024) {
            this.color = new Color(225,204,153);
        } else if (number == 2048) {
            this.color = new Color(153, 225, 153);
        } else {
            this.color = new Color(152, 152, 211);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tile tile = (Tile) o;
        return number == tile.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "" + number;
    }

}
