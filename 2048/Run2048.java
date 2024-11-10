package org.cis1200.game2048;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Run2048 implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 300);


        // Status panel
        final JLabel score = new JLabel("Score: 0  ",SwingConstants.RIGHT);
        score.setFont(new Font("Tahoma",Font.BOLD,20));
        score.setForeground(new Color(117, 156, 255));

        final JLabel username = new JLabel("  ",SwingConstants.LEFT);
        username.setFont(new Font("Tahoma",Font.PLAIN,20));
        username.setForeground(new Color(96, 96, 96));

        final JPanel userAndScore = new JPanel(new BorderLayout());
        userAndScore.add(username, BorderLayout.WEST);
        userAndScore.add(score, BorderLayout.EAST);


        // Game board
        final GameBoard board = new GameBoard(score);
        board.reset();
        frame.add(board, BorderLayout.CENTER);


        final JPanel control_panel = new JPanel();

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        reset.setFont(new Font("Tahoma",Font.BOLD,15));
        reset.setForeground(new Color(96, 96, 96));
        reset.setBackground(new Color(190, 255, 233));
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.save());
        save.setFont(new Font("Tahoma",Font.BOLD,15));
        save.setForeground(new Color(96, 96, 96));
        save.setBackground(new Color(255, 201, 201));
        control_panel.add(save);


        final JPanel title_panel = new JPanel(new BorderLayout());
        final JLabel title = new JLabel("2048", SwingConstants.CENTER);
        title.setFont(new Font("TimesRoman",Font.BOLD,70));
        title.setForeground(new Color(152, 152, 211));
        title_panel.add(title,BorderLayout.CENTER);
        title_panel.add(userAndScore,BorderLayout.SOUTH);
        frame.add(title_panel,BorderLayout.NORTH);

        frame.add(control_panel,BorderLayout.SOUTH);



//**************************************************************************************

        //welcome frame
        final JFrame welcome = new JFrame("2048 Welcome");
        welcome.setLocation(300, 300);

        final JPanel text_panel = new JPanel(new BorderLayout());
        final JLabel title1 = new JLabel("2048", SwingConstants.CENTER);
        title1.setFont(new Font("TimesRoman",Font.BOLD,70));
        title1.setForeground(new Color(126, 171, 159));
        text_panel.add(title1,BorderLayout.CENTER);

        final JLabel pleaseEnter = new JLabel("Please enter your username to get started",
                SwingConstants.CENTER);
        pleaseEnter.setFont(new Font("Tahoma",Font.BOLD,18));
        pleaseEnter.setForeground(new Color(96, 96, 96));
        text_panel.add(pleaseEnter,BorderLayout.SOUTH);

        final JPanel userEnter = new JPanel();

        final JPanel userWithText = new JPanel(new BorderLayout());

        final JTextField user = new JTextField(16);
        user.setFont(new Font("Tahoma",Font.PLAIN,15));
        userEnter.add(user);

        final JLabel userText = new JLabel("");
        userText.setFont(new Font("Tahoma",Font.BOLD,18));
        userText.setForeground(new Color(96, 96, 96));
        userWithText.add(userText, BorderLayout.SOUTH);


        final JButton submit = new JButton("submit");

        submit.addActionListener(e -> {
            String s = user.getText();
            int i = board.setUser(s);
            if (i == 1) {
                userText.setText("Invalid username");
            } else if (i == 2) {
                userText.setText("Running saved game");
                board.runSavedGame();
                username.setText("  " + s);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } else {
                userText.setText("Starting new game");
                username.setText("  " + s);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                // Start the game
                board.reset();
            }
            user.setText("");
        });
        submit.setFont(new Font("Tahoma",Font.BOLD,15));
        submit.setForeground(new Color(96, 96, 96));
        submit.setBackground(new Color(255, 201, 201));
        userEnter.add(submit);
        userWithText.add(userEnter,BorderLayout.CENTER);

        final JLabel howToPlay = new JLabel(
                "<html>HOW TO PLAY: Use your arrow keys to move the tiles. <br>" +
                "Tiles with the same number merge into one when they <br>touch. " +
                "Add them up to reach 2048!<html>"
                );
        howToPlay.setFont(new Font("Tahoma",Font.PLAIN,18));
        howToPlay.setForeground(new Color(96, 96, 96));
        welcome.add(howToPlay, BorderLayout.SOUTH);

        welcome.add(text_panel, BorderLayout.NORTH);
        welcome.add(userWithText,BorderLayout.CENTER);


        welcome.pack();
        welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcome.setVisible(true);



    }
}