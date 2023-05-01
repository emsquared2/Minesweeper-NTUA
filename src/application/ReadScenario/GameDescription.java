package application.ReadScenario;

/**
 * A class that describes the attributes of the game 
 * and defines setters and getters for these attributes.
 * Attributes:
 *      -> difficulty level (1 or 2)
 *      -> number of mines (9-11 for level 1 and 35-45 for level 2)
 *      -> maximum time the player has to reveal each tile without losing (120-180 secs for level 1 and 240-360 secs for level 2)
 *      -> number of supermines (0 for level 1 and 0 or 1 for level 2)
 */

public class GameDescription {

    private int difficultyLevel;    // the difficulty level of the game
    private int numMines;           // the number of mines in the game
    private int maxTime;            // the maximum time the player has to reveal each tile without losing
    private boolean hasSupermine;   // true if the game has a supermine, false otherwise
    
    // setters for the attributes

    /**
     * Sets the difficulty level of the game.
     * @param difficultyLevel the difficulty level to set (1 or 2)
     */
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Sets the number of mines in the game.
     * @param numMines the number of mines in the game to set (9-11 for level 1 and 35-45 for level 2)
     */
    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    /**
     * Sets the maximum time the player has to reveal each tile wihout losing.
     * @param maxTime  the maximum time to set (120-180 secs for level 1 and 240-360 secs for level 2).
     */
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * Sets whether the game has a supermine.
     * @param hasSupermine true if the game has a supermine, false otherwise
     */
    public void setHasSupermine(boolean hasSupermine) {
        this.hasSupermine = hasSupermine;
    }

    // getters for the attributes

    /**
     * Returns the difficulty level of the game.
     * @return the difficulty level of the game
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Returns the number of mines in the game.
     * @return the number of mines in the game.
     */
    public int getNumMines() {
        return numMines;
    }

    /**
     * Returns the maximum time the player has to reveal each tile without losing.
     * @return the maximum time the player has to reveal each tile without losing.
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * Returns whether the game has a supermine.
     * @return true if the game has a supermine, false otherwise
     */
    public boolean getHasSupermine() {
        return hasSupermine;
    }
}