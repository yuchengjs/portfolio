package org.cis1200.game2048;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 */
public class Game2048 {

    private Tile[][] board;
    private int size;
    private int score;
    private boolean gameOver;

    private boolean won;

    private String username;

    private String filePath;

    private static TreeMap<String, String> saved = new TreeMap<>();

    /**
     * Constructor sets up game state.
     */
    public static void resetSaved() {
        saved = new TreeMap<>();
    }

    public Game2048() {
        reset();
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public int getScore() {
        return score;
    }

    public boolean isWon() {
        return won;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public String getUsername() {
        return username;
    }

    public boolean notSameBoard(Tile[][] board1, Tile[][] board2) {
        return !Arrays.deepEquals(board1, board2);
    }

    public Tile[][] cloneBoard() {
        Tile[][] ret = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret[i][j] = new Tile(board[i][j].getNumber());
            }
        }
        return ret;
    }

    public ArrayList<Tile> merge(ArrayList<Tile> sequence) {
        int currInt = 0;
        while (currInt < sequence.size() - 1) {
            Tile curr = sequence.get(currInt);
            Tile comp = sequence.get(currInt + 1);
            if (curr.equals(comp)) {
                int newNum = curr.getNumber() + comp.getNumber();
                score += newNum;
                curr.setNumber(newNum);
                sequence.remove(comp);
                if (newNum == 2048) {
                    won = true;
                }
            }
            currInt++;
        }
        return sequence;
    }

    // Takes in boolean gen (if true, then generate new tile) for testing purposes.
    public void up(boolean gen) {
        Tile[][] board2 = cloneBoard();
        for (int col = 0; col < size; col++) {
            ArrayList<Tile> sequence = new ArrayList<>();
            for (int row = 0; row < size; row++) {
                if (board[row][col].getNumber() != 0) {
                    sequence.add(new Tile(board[row][col].getNumber()));
                    board[row][col].setNumber(0);
                }
            }
            sequence = merge(sequence);
            int row = 0;
            for (Tile t : sequence) {
                board[row][col] = t;
                row++;
            }
        }
        if (gen) {
            if (notSameBoard(board, board2)) {
                generate();
            }
        }
    }

    public void down(boolean gen) {
        Tile[][] board2 = cloneBoard();
        for (int col = 0; col < size; col++) {
            ArrayList<Tile> sequence = new ArrayList<>();
            for (int row = size - 1; row >= 0; row--) {
                if (board[row][col].getNumber() != 0) {
                    sequence.add(new Tile(board[row][col].getNumber()));
                    board[row][col].setNumber(0);
                }
            }
            sequence = merge(sequence);
            int row = size - 1;
            for (Tile t : sequence) {
                board[row][col] = t;
                row--;
            }
        }
        if (gen) {
            if (notSameBoard(board, board2)) {
                generate();
            }
        }
    }

    public void right(boolean gen) {
        Tile[][] board2 = cloneBoard();
        for (int row = 0; row < size; row++) {
            ArrayList<Tile> sequence = new ArrayList<>();
            for (int col = size - 1; col >= 0; col--) {
                if (board[row][col].getNumber() != 0) {
                    sequence.add(new Tile(board[row][col].getNumber()));
                    board[row][col].setNumber(0);
                }
            }
            sequence = merge(sequence);
            int col = size - 1;
            for (Tile t : sequence) {
                board[row][col] = t;
                col--;
            }
        }
        if (gen) {
            if (notSameBoard(board, board2)) {
                generate();
            }
        }
    }

    public void left(boolean gen) {
        Tile[][] board2 = cloneBoard();
        for (int row = 0; row < size; row++) {
            ArrayList<Tile> sequence = new ArrayList<>();
            for (int col = 0; col < size; col++) {
                if (board[row][col].getNumber() != 0) {
                    sequence.add(new Tile(board[row][col].getNumber()));
                    board[row][col].setNumber(0);
                }
            }
            sequence = merge(sequence);
            int col = 0;
            for (Tile t : sequence) {
                board[row][col] = t;
                col++;
            }
        }
        if (gen) {
            if (notSameBoard(board, board2)) {
                generate();
            }
        }
    }

    // checks if game is over (no adjacent tiles are equal)
    public boolean isGameOver() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col].getNumber() == 0) {
                    gameOver = false;
                    return false;
                }
            }
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 1; col++) {
                if (board[row][col].equals(board[row][col + 1])) {
                    gameOver = false;
                    return false;
                }
            }
        }
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size - 1; row++) {
                if (board[row][col].equals(board[row + 1][col])) {
                    gameOver = false;
                    return false;
                }
            }
        }
        gameOver = true;
        return true;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nScore " + score + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 4) {
                    System.out.print(" | ");
                }
            }
            if (i < 4) {
                System.out.println("\n---------------");
            }
        }
    }

    public void generate() {

        Random rand = new Random();
        int x = rand.nextInt(0,size);
        int y = rand.nextInt(0,size);
        int value = rand.nextInt(10);
        while (board[x][y].getNumber() != 0) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }
        if (value < 1) {
            board[x][y].setNumber(4);
        } else {
            board[x][y].setNumber(2);
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        size = 4;
        board = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Tile();
            }
        }
        score = 0;
        won = false;
        gameOver = false;
        generate();
        generate();
    }



    public Tile getCell(int i, int j) {
        return board[i][j];
    }

    // Returns 1 if the username is invalid. Must be username without spaces or special characters
    //Returns 2 if username in use, 3 if new username created successfully.
    public int setUsername(String username) {
        if (saved.containsKey(username)) {
            this.username = username;
            getSaved(saved.get(username));
            this.filePath = saved.get(username);
            return 2;
        }
        this.filePath = "files/" + username + ".txt";
        this.username = username;
        try {
            File file = new File(filePath);
            file.createNewFile();
            save();
        } catch (IOException e) {
            return 1;
        }
        return 3;
    }

    public void getSaved(String filePath) {
        File file = Paths.get(filePath).toFile();
        BufferedReader br;
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            score = Integer.parseInt(br.readLine());
            for (int i = 0; i < size; i++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int j = 0; j < size; j++) {
                    board[i][j] = new Tile(Integer.parseInt(numbers[j]));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error while reading");
        }
    }

    public void save() {
        File file = Paths.get(filePath).toFile();
        BufferedWriter bw;
        try {
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(score + "");
            bw.newLine();
            for (Tile[] row : board) {
                for (Tile t : row) {
                    bw.write(t.getNumber() + " ");
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
            saved.put(username, filePath);
        } catch (IOException e) {
            System.out.println("Error while writing");
        }
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Game2048 g = new Game2048();
        g.printGameState();

        g.left(true);
        g.printGameState();

        g.right(true);
        g.printGameState();

        g.up(true);
        g.printGameState();

        g.down(true);
        g.printGameState();

        g.left(true);
        g.printGameState();

        g.right(true);
        g.printGameState();

        g.up(true);
        g.printGameState();

        g.down(true);
        g.printGameState();

    }
}