package application.GameObjects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


/**
 * The BoardGenerator class is responsible for generating the board of the game randomly,
 * based on the data in the scenario. It also writes the location and type of the mines in
 * the board to a file called mines.txt.
*/
public class BoardGenerator {

    // Constant for mine types
    public static final int MINE = -1;
    public static final int SUPER_MINE = -2;

    // Instance variables
    private Tile[][] board;
    private int boardSize;
    private int numMines;
    private boolean HasSupermine;

    /**
     * Constructor for BoardGenerator.
     * 
     * @param difficultyLevel the difficulty level of the game (1 or 2).
     * @param numMines        the number of mines to place on the board.
     * @param hasSupermine    whether or not the board has a supermine.
     */
    public BoardGenerator(int difficultyLevel, int numMines, boolean HasSupermine) {
        
        this.boardSize = ( (difficultyLevel == 1) ? 9 : 16 );
        this.numMines = numMines;
        this.HasSupermine = HasSupermine;
        this.board = new Tile[boardSize][boardSize]; 

        // Initialize the board with 0's
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                this.board[i][j] = new Tile(i, j, 0);
            }
        }
    }

    /**
     * Generates the game board based on the scenario's data.
     * 
     * @return the generated board as a 2D array of tiles.
     */
    public Tile[][] GenerateBoard() {

        /*
         *  Board is a 2D array of tiles where:
         *      board[x][y].getValue() = 0  => not mine
         *      board[x][y].getValue() = -1 => mine
         *      board[x][y].getValue() = -2 => supermine 
         */

        Random rand = new Random();
        int numPlacedMines = 0;

        // If superMine exists, then pick a random location to place the superMine
        if(HasSupermine) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);
            board[x][y].setValue(SUPER_MINE);
            numPlacedMines++;
        }

        // While there are mines to place, pick a random location and add a mine to the board
        while (numPlacedMines < numMines) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            // check if there is not mine or superMine in board[x][y] (i.e check if board[x][y].getValue() == 0)
            if(board[x][y].getValue() == 0) {
                board[x][y].setValue(MINE);
                numPlacedMines++;
            }
        }
        return board;
    }

    /**
     * Method to display the current state of the game board. This method is primarily used for testing purposes.
     */
    public void BoardPrinter() {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Function that writes the positions of mines (and the supermine, if it exists) to a file called "mines.txt" 
     * located in the "./mines" directory. 
     */
    public void MineRecorder() {
        try {
            FileWriter fw = new FileWriter("./mines/mines.txt");
            for(int i = 0; i < boardSize; i++) {
                for(int j = 0; j < boardSize; j++) {
                    if(board[i][j].getValue() != 0) {
                        int isSupermine = (board[i][j].getValue() == SUPER_MINE) ? 1 : 0;
                        fw.write(i + "," + j + "," + isSupermine + "\n");
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
