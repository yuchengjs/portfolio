package org.cis1200.game2048;

//import org.cis1200.game2048.GameObj;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Square extends GameObj {
    public static final int SIZE = 100;
 //   public static final int INIT_POS_X = 0;
 //   public static final int INIT_POS_Y = 0;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private final Color color;
    private int number;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Square(int initPosX, int initPosY,
                  int courtWidth, int courtHeight,
                  Color color, int number) {
        super(INIT_VEL_X, INIT_VEL_Y, initPosX, initPosY, SIZE, SIZE, courtWidth, courtHeight);
        this.color = color;
        this.number = number;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        g.setColor(new Color(96,96,96));
        g.setFont(new Font("TimesRoman",Font.BOLD,50));
        if (number < 16) {
            g.drawString(number + "",this.getPx() + 35,this.getPy() + 65);
        } else if (number < 128) {
            g.drawString(number + "",this.getPx() + 20,this.getPy() + 65);
        } else if (number < 1024) {
            g.setFont(new Font("TimesRoman",Font.BOLD,40));
            g.drawString(number + "",this.getPx() + 15,this.getPy() + 65);
        } else {
            g.setFont(new Font("TimesRoman",Font.BOLD,35));
            g.drawString(number + "",this.getPx() + 10,this.getPy() + 60);
        }

    }
}