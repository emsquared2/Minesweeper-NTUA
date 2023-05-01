package application.GameObjects;

import javafx.scene.control.Button;

/**
 * This class extends the Button class and adds some additional functionality related to our Minesweeper game.
 * Each Tile has a row and column index, an integer value indicating whether it is a mine (value = -1), 
 * a supermine (value = -2) or a simple tile (value = 0), and boolean flags indicating whether it has been
 * revealed or flagged by the player. It also has an integer value indicating the number of adjacent mines.
 */

public class Tile extends Button {
    
    public static final int MINE = -1;
    public static final int SUPER_MINE = -2;

    private int row;
    private int col;
    private int value;
    private int adjacentMines;
    private boolean isRevealed;
    private boolean isFlagged;


    /**
     * Constructor that initializes the tile with the given row, column and value.
     * Also sets the adjacent mines to zero and isRevealed and isFlagged to false.
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @param value an integer value that indicates whether the tile is mine (-1),
     *              a supermine (-2) or a simple tile (0)
     */
    public Tile(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.adjacentMines = 0;
        this.isRevealed = false;
        this.isFlagged = false;
        this.setPrefSize(30, 30); // set the preferred size of the button
        setStyle("-fx-background-color: darkmagenta");
    }
    
    /**
     * This method is called when the player left-clicks on a tile to reveal it.
     * It disables the button and changes its background image based on the tile value.
     * If the tile is a mine, sets the background image to a mine image.
     * If the tile is a supermine, sets the background image to a supermine image.
     * If the tile is a simple tile, sets the background image to a number image indicating
     * the number of adjacent mines.
     */
    public void reveal() {

        // If it was flagged, then unflag it
        if(isFlagged) {
            isFlagged = false;
        }
        
        isRevealed = true;  // reveal the tile
        setDisable(true);   // disable the button
        setStyle("-fx-background-color: lightgray"); // change the button's background color
        
        if (value == MINE) {
            setStyle("-fx-background-image: url(\"file:assets/images/mine.png\"); -fx-background-size: cover;");
        }
        else if (value == SUPER_MINE) {
            setStyle("-fx-background-image: url(\"file:assets/images/supermine.png\"); -fx-background-size: cover;");
        }
        else {
            String path = "file:assets/images/" + adjacentMines +".png";
            setStyle("-fx-background-image: url(\"" + path + "\"); -fx-background-size: cover;");
        }
    }

    /**
     * This method is called when the player right-clicks on a tile to toggle its flagged status.
     * It changes the isFlagged boolean value and sets the background image accordingly.
     */
    public void toggleFlag() {
        isFlagged = !isFlagged;
        // change the style (image) 
        if(isFlagged) {
            setStyle("-fx-background-image: url(\"file:assets/images/flag.png\"); -fx-background-size: cover;");
        }
        else {
            setStyle("-fx-background-color: darkmagenta");
        }
    }

    // getters

    /**
     * Returns the row index of the tile.
     * @return the row index of the tile
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of the tile.
     * @return the column index of the tile
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the value of the tile.
     * @return an integer value that indicates whether the tile is mine (-1),
     *        a supermine (-2) or a simple tile (0)
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the number of adjacent mines.
     * @return the number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Returns whether the tile has been revealed by the player.
     * @return true if the tile has been revealed, false otherwise
     */
    public boolean getIsRevealed() {
        return isRevealed;
    }

    /**
     * Returns whether the tile has been flagged by the player.
     * @return true if the tile has been flagged, false otherwise
     */
    public boolean getIsFlagged() {
        return isFlagged;
    }

    // setters

    /**
     * Sets the row index of the tile.
     * @param row the row index of the tile
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the col index of the tile.
     * @param col the col index of the tile
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Sets the value of the tile.
     * @param value an integer value that indicates whether the tile is mine (-1),
     *              a supermine (-2) or a simple tile (0)
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Sets the number of adjacent mines.
     * @param adjacentMines the number of adjacent mines
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    /**
     * Sets the revealed status of the tile.
     * @param isRevealed true if the tile has been revealed, false otherwise
     */
    public void setIsRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    /**
     * Sets the flagged status of the tile.
     * @param isFlagged true if the tile has been flagged, false otherwise
     */
    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }
}
