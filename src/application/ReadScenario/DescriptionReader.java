package application.ReadScenario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is responsible for checking the validity of the SCENARIO-ID.txt file
 * and storing the game's attributes using the setters from the GameDescription class.
 * It contains a static method readDescription which reads the input file and returns
 * a GameDescription object. The method may throw InvalidDescriptionException,
 * InvalidValueException, or IOException, which will be handled later.
*/
public class DescriptionReader {

    /**
     * Reads the input file and returns a GameDescription object. 
     * Throws InvalidDescriptionException, InvalidValueException, or IOException if 
     * there are any issues with the input file or values in the file.
     * 
     * @param filePath the path of the input file
     * @return a GameDescription object
     * @throws InvalidDescriptionException if the input file is missing required information
     * @throws InvalidValueException if the input file contains invalid values
     * @throws IOException if there is an issue reading the input file
     */
    public static GameDescription readDescription(String filePath) throws InvalidDescriptionException, InvalidValueException, IOException {
        
        GameDescription gameDescription = new GameDescription();
            
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
        try {
            // read each line
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();
            String line4 = reader.readLine();

            if (line1 == null) {
                throw new InvalidDescriptionException("Description file is missing difficulty level!");
            }
            if (line2 == null) {
                throw new InvalidDescriptionException("Description file is missing number of mines!");
            }
            if (line3 == null) {
                throw new InvalidDescriptionException("Description file is missing maximum time");
            }
            if (line4 == null) {
                throw new InvalidDescriptionException("Description file is missing supermine information");
            }
    
            int difficultyLevel = Integer.parseInt(line1);
            if(difficultyLevel != 1 && difficultyLevel != 2) {
                throw new InvalidValueException("Invalid difficulty level! Difficulty level should be 1 or 2!");
            }
            gameDescription.setDifficultyLevel(difficultyLevel);
            

            int numMines = Integer.parseInt(line2);
            if (difficultyLevel == 1 && (numMines < 9 || numMines > 11)) {
                throw new InvalidValueException("Number of mines is not within acceptable limits for difficulty level 1");
            } else if (difficultyLevel == 2 && (numMines < 35 || numMines > 45)) {
                throw new InvalidValueException("Number of mines is not within acceptable limits for difficulty level 2");
            }
            gameDescription.setNumMines(numMines);
            
    
            int maxTime = Integer.parseInt(line3);
            if (difficultyLevel == 1 && (maxTime < 120 || maxTime > 180)) {
                throw new InvalidValueException("Maximum time is not within acceptable limits for difficulty level 1");
            } else if (difficultyLevel == 2 && (maxTime < 240 || maxTime > 360)) {
                throw new InvalidValueException("Maximum time is not within acceptable limits for difficulty level 2");
            }
            gameDescription.setMaxTime(maxTime);
    
    
            int supermine = Integer.parseInt(line4);
            if (supermine != 0 && supermine != 1) {
                throw new InvalidValueException("Invalid supermine value!");
            }
            if (difficultyLevel == 1 && supermine != 0) {
                throw new InvalidValueException("Difficulty level 1 cannot have a supermine");
            }
            gameDescription.setHasSupermine(supermine == 1);
        } finally {
            reader.close();
        }  
        return gameDescription;
    }
}