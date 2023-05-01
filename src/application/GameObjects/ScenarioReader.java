package application.GameObjects;

import java.io.IOException;

import application.ReadScenario.DescriptionReader;
import application.ReadScenario.GameDescription;
import application.ReadScenario.InvalidDescriptionException;
import application.ReadScenario.InvalidValueException;


/**
 * This class implements the method Read which reads SCENARIO-ID.txt using methods from the classes under folder "ReadScenario"
 * and returns a proper GameDescription object.
 */
public class ScenarioReader {

    GameDescription game = new GameDescription();

    /**
     * Reads SCENARIO-ID.txt and returns a GameDescription object.
     * @param iD the ID of the scenario to read
     * @return a GameDescription object
     * @throws InvalidDescriptionException if the description file is invalid
     * @throws InvalidValueException if the description file contains an invalid value
     * @throws IOException if an IO error occurs while reading the file
     */
    public GameDescription Read(String iD) throws InvalidDescriptionException, InvalidValueException, IOException {

        //read SCENARIO-ID.txt
        String filePath = "./medialab/SCENARIO-" + iD + ".txt";
        game = DescriptionReader.readDescription(filePath);
        return game; 
    }
}