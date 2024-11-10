package org.cis1200.game2048;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Game2048 game; // model for the game
    private JLabel score; // current status text

    private boolean lost;

    private boolean won;

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    public static final int SQUARE_VELOCITY = 15;
    public static final int INTERVAL = 35;

    private Square[][] squares;

    public void setScore() {
        score.setText("Score: " + game.getScore() + "  ");
    }

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel score) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        game = new Game2048(); // initializes model for the game
        this.score = score; // initializes the status JLabel



        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    game.left(true);
                    updateStatus();
                    repaint();

                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    game.right(true);
                    updateStatus();
                    repaint();

                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.down(true);
                    updateStatus();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.up(true);
                    updateStatus();
                    repaint();
                }
            }
        });

      //  updateStatus(); // updates the status JLabel
         // repaints the game board
           /*
           public void keyReleased(KeyEvent e) {
               square.setVx(0);
               square.setVy(0);
           }

            */
    }

    public void updateStatus() {
        score.setText("Score: " + game.getScore() + "  ");
        if (game.isGameOver()) {
            this.lost = true;
        }
        if (game.isWon()) {
            this.won = true;
        }
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        game.reset();
        repaint();
        score.setText("Score: 0  ");
        this.lost = false;
        this.won = false;
        squares = new Square[game.getSize()][game.getSize()];
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                int initX = i * 100;
                int initY = j * 100;
                Tile t = game.getCell(j,i);
                if (t.getNumber() != 0) {
                    squares[i][j] = new Square(initX,initY,
                            BOARD_WIDTH,BOARD_HEIGHT,t.getColor(),t.getNumber());
                }
            }
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void runSavedGame() {
        repaint();
        squares = new Square[game.getSize()][game.getSize()];
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                int initX = i * 100;
                int initY = j * 100;
                Tile t = game.getCell(j,i);
                if (t.getNumber() != 0) {
                    squares[i][j] = new Square(initX,initY,
                            BOARD_WIDTH,BOARD_HEIGHT,t.getColor(),t.getNumber());
                }
            }
        }
        updateStatus();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void save() {
        game.save();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(400, 0, 400, 400);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);
        g.drawLine(0, 400, 400, 400);


        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                int initX = i * 100;
                int initY = j * 100;
                Tile t = game.getCell(j,i);
                if (t.getNumber() != 0) {
                    Square square = new Square(initX,initY,
                            BOARD_WIDTH,BOARD_HEIGHT,t.getColor(),t.getNumber());
                    squares[i][j] = square;
                    square.draw(g);
                }
            }
        }
        if (this.won) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman",Font.BOLD,60));
            g.drawString("You win!", 65,200);
        } else if (this.lost) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman",Font.BOLD,60));
            g.drawString("Game over!", 35,200);
        }

    }

    public int setUser(String username) {
        return game.setUsername(username);
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
