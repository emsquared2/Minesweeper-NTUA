package application;

import application.GUI.MinesweeperGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The Minesweeper class is the entry point of the application. It extends the JavaFX Application class
 * and overrides the start method to initialize and display the MinesweeperGUI.
 */
public class Minesweeper extends Application {


    /**
     * The start method is called when the application is launched. It initializes the MinesweeperGUI,
     * creates a new BorderPane and adds the MinesweeperGUI to the center of the BorderPane. It then
     * creates a new Scene with the BorderPane as the root and sets the Scene of the primary stage.
     * Finally, it shows the primary stage.
     *
     * @param stage the primary stage of the application
     */
    @Override
    public void start(Stage stage) {

        // Create an instance of the StartPage class
        MinesweeperGUI startPage = new MinesweeperGUI();

        // Create a new BorderPane and add the StartPage to the center
        BorderPane root = new BorderPane();
        root.setCenter(startPage);

        // Create a new Scene with the BorderPane as the root
        Scene scene = new Scene(root, 800, 600);

        // Set the title of the window
        stage.setTitle("MediaLab Minesweeper");

        // Set the Scene of the primary stage
        stage.setScene(scene);

        // Show the primary stage
        stage.show();
    }

    /**
      * The main method is the entry point of the application. It calls the launch method of the Application
      * class and passes the command line arguments.
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        launch(args);
    }
}
