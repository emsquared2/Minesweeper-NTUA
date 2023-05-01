package application.GUI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class implements the "Rounds" option functionality.
 * It retrieves information about the previous rounds and displays it in a pop-up window.
 */
public class RoundsAction {

    // The prefix and extension for the round files.
    private static final String FILE_PREFIX = "round-";
    private static final String FILE_EXTENSION = ".txt";
    
    /**
     * Retrieves information about the previous rounds and displays it in a pop-up window.
     */
    public static void handleRoundOption() {

        // Get the path to the rounds folder
        Path roundsDirPath = Paths.get("rounds");
        if (!Files.exists(roundsDirPath)) {
            // If the directory doesn't exist, then no related data exist too, 
            // so display a pop up error window.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("No Data Found");
            alert.setContentText("There are no previous rounds.");
            alert.showAndWait();
        } else {
        
            // Obtain each file under folder "rounds" and sort them according to the last modified timestamp
            File[] roundFiles = roundsDirPath.toFile().listFiles((dir, name) -> name.startsWith(FILE_PREFIX) && name.endsWith(FILE_EXTENSION));
            List<File> sortedFiles = Arrays.asList(roundFiles);
            sortedFiles.sort(Comparator.comparing(File::lastModified));

            if (roundFiles == null || roundFiles.length == 0) {
                // If there are no files then display a pop up error window.
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("No Data Found");
                alert.setContentText("There are no previous rounds.");
                alert.showAndWait();   
            }
            else {
                // Display a pop up window with information about the last 5 rounds
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(10));
                vBox.setSpacing(10);

                List<String> fileContents = new ArrayList<>();

                for (File file : roundFiles) {
                    try {
                        byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                        String content = new String(bytes);;
                        fileContents.add(content);
                    } catch (IOException e) {
                        // Handle file read error
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < fileContents.size(); i++) {
                    String content = "Round " + (i + 1) + ":\n" + fileContents.get(i);
                    Label label = new Label(content);
                    vBox.getChildren().add(label);
                }                

                Scene scene = new Scene(vBox, 650, 550);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Last 5 rounds - Info");
                stage.show();
            }
        }
    }
}
