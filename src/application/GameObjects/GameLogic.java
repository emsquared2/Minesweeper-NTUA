package application.GameObjects;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class implements the game's logic.
 * The main methods of this class will be called each time the player interacts with the game (right or left click etc).
*/

public class GameLogic {
    
    public static final int MINE = -1;
    public static final int SUPER_MINE = -2;
    
    /**
     * board            -> The game board
     * boardsize        -> The size of the board (9 for level 1 and 16 for level 2)
     * numMines         -> Total number of mines
     * MarkedTiles      -> Number of tiles marked as mines
     * numRevealed      -> The number of revealed tiles
     * maxTime          -> Maximum available time the player has 
     * timeLeft         -> Time left for player before they exceed the maximum allowed time (maxTime)
     * gameOver         -> Indicates if the game is over
     * gameWon          -> Indicates if the player won
     * gameLost         -> Indicates if the player lost
     * attempts         -> Number of player's clicks. Used in flagTile for the flagging of the supermine
     * StartedTimer     -> This flag is used to check if the timer has started
     */

    private Tile[][] board;
    private int boardsize;
    private int numMines;
    private int MarkedTiles;
    private int numRevealed;
    private int maxTime;
    private long timeLeft;
    private boolean gameOver;
    private boolean gameWon;
    private boolean gameLost;
    private int attempts;
    private boolean flagSuperMine;
    private Timer timer;
    private boolean StartedTimer;

    
    /**
     * Constructs a new instance of the GameLogic class.
     * @param board The game board represented as a 2D array of Tile objects.
     * @param numMines The total number of mines in the game.
     * @param maxTime The maximum amount of time the player has to complete the game.
     */
    public GameLogic(Tile[][] board, int numMines, int maxTime) {
        this.board = board;
        this.boardsize = board.length;
        this.numMines = numMines;
        this.maxTime = maxTime;
        this.MarkedTiles = 0;
        this.numRevealed = 0;
        this.timeLeft = maxTime;
        this.gameOver = false;
        this.gameWon = false;
        this.gameLost = false;
        this.flagSuperMine = false;
        this.attempts = 0;
        this.StartedTimer = false;
    }

    /**
     * Recursive function that reveals a tile when the player left-clicks on it. This method is called
     * from the AddRevealClick method and is used to reveal the tile and update game variables based on
     * the game rules.
     * @param row The row of the tile to reveal.
     * @param col The column of the tile to reveal.
     * @param recursion A boolean flag that controls the recursive revealing of adjacent tiles.
     */
    private void revealTile(int row, int col, boolean recursion) {
        
        // If tile is already revealed or if the game is already finished, then do nothing
        if(board[row][col].getIsRevealed() || gameOver) {
            return;
        }

        // If the tile is a mine, then the game ends and we reveal every mine
        if(board[row][col].getValue() == MINE || board[row][col].getValue() == SUPER_MINE) {
            RevealMines();
            gameLost = true;
            EndGame();
            return;
        }

        // Check if the tile is flagged and if it is, then decrease the number of marked tiles
        if(board[row][col].getIsFlagged()) {
            MarkedTiles--;
        }

        // Count the number of adjacent mines
        int mineCount = getNumAdjacentMines(row, col);

        // Update the related field
        board[row][col].setAdjacentMines(mineCount);

        // Reveal the tile and update the counter of the revealed tiles;
        board[row][col].reveal();
        numRevealed++;

        // Check if GameWon and GameOver flags need update
        CheckIfGameWon();

        // If there are no adjacent mines, then recursively reveal adjacent tiles
        if(mineCount == 0) {
            if(recursion) { 
                for(int i = row - 1; i <= row + 1; i++) {
                    for(int j = col - 1; j <= col + 1; j++) {
                        if(i >= 0 && i < boardsize && j >= 0 && j < boardsize) {
                            revealTile(i,j, true);
                        }
                    }
                }
            }
        }
        return;
    }

    /**
     * Due to the fact that revealTile is recursive, we can't use it to count the number of user's attempts. 
     * So, we use this mwthod which calls revealTile and then updates attempts. 
     * This method is called each time the player left-clicks on a tile to reveal it.
     * @param row The row index of the tile being clicked.
     * @param col The column index of the tile being clicked.
     */
    public void AddRevealClick(int row, int col) {
        if (!StartedTimer) {
            startTimer();
            StartedTimer = true;
        }
        attempts++;
        revealTile(row, col, true);
    } 

    /**
     * This method is called when the player right-clicks on a tile to mark (or unmark) it as mine.
     * If the player marks the tile corresponding to the supermine within the first 4 attempts,
     * then the application should automatically reveal the content of all the tiles that
     * are in the same row and column as the super-mine.
    @param row The row index of the tile
    @param col The column index of the tile
     */
    public void flagTile(int row, int col) {

        // If the timer has not started yet and current tile is supermine, then start the timer.
        if(!StartedTimer && board[row][col].getValue() == SUPER_MINE) {
            startTimer();
            StartedTimer = true;
        }

        // If the game is over or the tile is already revealed then do nothing.
        if(gameOver || board[row][col].getIsRevealed()) {
            return;
        }
        // If the maximum number of flags has been reached, then if the tile is not flagged, return because we can't use extra flags.
        if(MarkedTiles == numMines && !board[row][col].getIsFlagged()) {
            return;
        }

        // If the tile is already flagged, then unflag it and decrease the related counter.
        if(board[row][col].getIsFlagged()) {
            board[row][col].toggleFlag();
            MarkedTiles--;
            return;
        }

        // Otherwise, flag it and increase the related counter
        board[row][col].toggleFlag();
        MarkedTiles++;

        /* If the player marks the tile corresponding to the supermine within the first 4 attempts, 
        *  then the application should automatically reveal the content of all the tiles that 
        *  are in the same row and column as the super-mine.
        */
        if(board[row][col].getValue() == SUPER_MINE && attempts < 4 && !flagSuperMine) {

            // this flag is used to allow only 1 access in this part of the code
            flagSuperMine = true;

            // Check if it is flagged and if it is, call flagTile to unflag it.
            if(board[row][col].getIsFlagged()) {
                flagTile(row, col);
            }

            // Reveal supermine
            board[row][col].reveal();

            // Reveal each tile in the same row or column
            for(int i = 0; i < boardsize; i++) {

                if(i != row) {
                    // If tile is a mine, then reveal it without calling revealTile
                    if(board[i][col].getValue() == MINE ) {

                        // Check if it is flagged and if it is, call flagTile to unflag it.
                        if(board[i][col].getIsFlagged()) {
                            flagTile(i, col);
                        }
                        board[i][col].reveal();
                    }
                    else {
                        // flag recursion is false, because we don't want to recursively reveal adjacent tiles
                        revealTile(i, col, false);
                    }
                }

                if(i != col) {
                    // If tile is a mine, then reveal it without calling revealTile
                    if(board[row][i].getValue() == MINE && (i != col) ) {

                        // Check if it is flagged and if it is, call flagTile to unflag it.
                        if(board[row][i].getIsFlagged()) {
                            flagTile(row, i);
                        }
                        board[row][i].reveal();
                    }
                    else {
                        // flag recursion is false, because we don't want to recursively reveal adjacent tiles
                        revealTile(row, i, false);
                    }
                }
            }
        }
    }

    /**
     * Reveals all the mines on the game board.
     */
    private void RevealMines() {
        for(int i = 0; i < boardsize; i++) {
            for(int j = 0; j < boardsize; j++) {
                if(board[i][j].getValue() == MINE || board[i][j].getValue() == SUPER_MINE) {
                    board[i][j].reveal();
                } 
            }
        }
    }
    
    /**
     * Ends the game by setting the gameOver flag to true, stopping the timer, and storing the round's data.
     */
    private void EndGame() {
        gameOver = true;
        stopTimer();

        String winner = (gameWon) ? "Player" : "PC";

        try {
            // Store this round's data
            FileManager.writeCurrentRound(numMines, attempts, maxTime - timeLeft, winner);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * Returns the number of adjacent mines for a given tile on the game board.
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return the number of adjacent mines
     */
    private int getNumAdjacentMines(int row, int col) {
        int mineCount = 0;
        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = col -1; j <= col + 1; j++) {
                // This if statement takes into account the corner and side tiles of the board
                if (i >= 0 && i < boardsize && j >= 0 && j < boardsize && (board[i][j].getValue() == MINE || board[i][j].getValue() == SUPER_MINE) ) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    /**
     * Checks if the game is won by the player when all squares without mines are revealed and timeLeft is not zero.
     * If the game is won, sets gameWon flag to true and ends the game.
     */
    private void CheckIfGameWon() {
        if(numRevealed + numMines == boardsize * boardsize && timeLeft > 0) {
            gameWon = true;
            EndGame();
        }
    }

    /**
     * Starts the timer and decreases timeLeft by 1 every second until timeLeft is zero.
     * If timeLeft == 0, sets gameLost flag to true and ends the game.
     */
    public void startTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                if(timeLeft > 0) {
                    timeLeft--;
                }
                // If timeLeft == 0, then the player lose.
                else {
                    gameLost = true;
                    EndGame();
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    /**
     * This method reveals every mine (and the supermine).
     * It is called when the Solution option is selected.
     * If the game has not started yet, an error message is displayed and the method is exited.
     */
    public void Solution() {
        if(attempts == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to reveal a solution.");
            alert.setContentText("The game has not started yet. Open a tile in order to start the game.");
            alert.showAndWait();
            return;
        }
        RevealMines();
        gameLost = true;
        EndGame();
        return;
    }

    /** 
     * Stops the timer. 
     */ 
    private void stopTimer() {
        timer.cancel();
    }
    
    // Getters

    /**
     * Returns the game board.
     * @return the game board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Returns the size of the board.
     * @return the size of the board
     */
    public int getBoardsize() {
        return boardsize;
    }

    /**
     * Returns the number of mines in the game.
     * @return the number of mines in the game
     */
    public int getNumMines() {
        return numMines;
    }

    /**
    * Returns the number of marked tiles.
    * @return the number of marked tiles
    */
    public int getMarkedTiles() {
        return MarkedTiles;
    }

    /**
     * Returns the number of revealed tiles.
     * @return the number of revealed tiles
     */
    public int getNumRevealed() {
        return numRevealed;
    }

    /**
     * Returns the maximum time allowed for the game.
     * @return the maximum time allowed for the game
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * Returns the time left for the game.
     * @return the time left for the game
     */
    public long getTimeLeft() {
        return timeLeft;
    }

    /**
     * Returns whether the game is over or not.
     * @return true if the game is over, false otherwise
     */
    public boolean getGameOver() {
        return gameOver;
    }
    
    /**
     * Returns whether the player has won the game or not.
     * @return true if the player has won, false otherwise
     */
    public boolean getGameWon() {
        return gameWon;
    }

    /**
     * Returns whether the player has lost the game or not.
     * @return true if the player has lost, false otherwise
     */
    public boolean getGameLost() {
        return gameLost;
    }

    /**
    * Returns the number of attempts the player has made to win the game.
    * @return the number of attempts the player has made to win the game
    */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Returns the timer used to keep track of time in the game.
     * @return the timer used to keep track of time in the game
     */
    public Timer getTimer() {
        return timer;
    }
}