package application.GUI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import application.GameObjects.BoardGenerator;
import application.GameObjects.GameLogic;
import application.GameObjects.ScenarioReader;
import application.GameObjects.Tile;
import application.ReadScenario.GameDescription;
import application.ReadScenario.InvalidDescriptionException;
import application.ReadScenario.InvalidValueException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class implements the Minesweeper game GUI:
 * It displays a welcome message.
 * It implements the menu bar and options and their functionality.
 * It displays the main screen.
*/
public class MinesweeperGUI extends BorderPane {

    public static final int MINE = -1;
    public static final int SUPER_MINE = -2;

    private int difficultyLevel;
    private int numMines;
    private int maxTime;
    private boolean hasSupermine;
    private Tile[][] board;
    private GameLogic gameLogic;
    private boolean Loaded;     // Used to check if a scenario is loaded
    private boolean Playing;    // Used to check if player is playing

    
    public MinesweeperGUI() {
        
        Loaded = false;
        Playing = false;

        // Create the menu bar and menus
        MenuBar menuBar = new MenuBar();
        Menu applicationMenu = new Menu("Application");
        Menu detailsMenu = new Menu("Details");

        // Create the menu items for the application menu
        MenuItem createMenuItem = new MenuItem("Create");
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem startMenuItem = new MenuItem("Start");
        MenuItem exitMenuItem = new MenuItem("Exit");

        // Add the menu items to the application menu
        applicationMenu.getItems().addAll(createMenuItem, loadMenuItem, startMenuItem, new SeparatorMenuItem(), exitMenuItem);

        // Create the menu items for the details menu
        MenuItem roundsMenuItem = new MenuItem("Rounds");
        MenuItem solutionMenuItem = new MenuItem("Solution");

        // Add the menu items to the details menu
        detailsMenu.getItems().addAll(roundsMenuItem, solutionMenuItem);

        // Add the menus to the menu bar
        menuBar.getMenus().addAll(applicationMenu, detailsMenu);

        // Set the style of the menu bar
        menuBar.setBackground(new Background(new BackgroundFill(Color.DARKMAGENTA, CornerRadii.EMPTY, Insets.EMPTY)));

        // Set the style of the menu items
        applicationMenu.setStyle("-fx-text-fill: white;");
        detailsMenu.setStyle("-fx-text-fill: white;");
        createMenuItem.setStyle("-fx-text-fill: black;");
        loadMenuItem.setStyle("-fx-text-fill: black;");
        startMenuItem.setStyle("-fx-text-fill: black;");
        exitMenuItem.setStyle("-fx-text-fill: black;");
        roundsMenuItem.setStyle("-fx-text-fill: black;");
        solutionMenuItem.setStyle("-fx-text-fill: black;");

        // Add action listeners to the menu items

        // If we select Exit, the app will shut down
        exitMenuItem.setOnAction(e -> {
            System.exit(0);
        });

        /* 
         * If we select Create, a pop up window will appear, asking for game details/description.
         * After pressing Create, the SCENARIO-ID.txt is created under folder medialab.
         * ID is required.
         * Level, number of mines and max time must be integers. 
         */
        createMenuItem.setOnAction(e -> {

            // Create a new dialog for the scenario details
            Dialog<String[]> dialog = new Dialog<>();
            dialog.setTitle("Create new scenario");
            dialog.setHeaderText("Please enter scenario details:");
        
            // Create the dialog content
            Label idLabel = new Label("ID: ");
            Label levelLabel = new Label("Level (1 or 2): ");
            Label minesLabel = new Label("Total number of mines: ");
            Label supermineLabel = new Label("Supermine?: ");
            Label maxTimeLabel = new Label("Maximum time (secs): ");
        
            TextField idTextField = new TextField();
            TextField levelTextField = new TextField();
            TextField minesTextField = new TextField();
            ChoiceBox<String> supermineChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Yes", "No"));
            supermineChoiceBox.setValue("No"); // set default value to no
            TextField maxTimeTextField = new TextField();
        
            GridPane grid = new GridPane();
            grid.add(idLabel, 1, 1);
            grid.add(idTextField, 2, 1);
            grid.add(levelLabel, 1, 2);
            grid.add(levelTextField, 2, 2);
            grid.add(minesLabel, 1, 3);
            grid.add(minesTextField, 2, 3);
            grid.add(supermineLabel, 1, 4);
            grid.add(supermineChoiceBox, 2, 4);
            grid.add(maxTimeLabel, 1, 5);
            grid.add(maxTimeTextField, 2, 5);
            dialog.getDialogPane().setContent(grid);
        
            // Add buttons to the dialog
            ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
            // Convert the input into a Scenario object when the Create button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    String id = idTextField.getText();
                    String level = levelTextField.getText();
                    String mines = minesTextField.getText();
                    String supermine = supermineChoiceBox.getValue().equalsIgnoreCase("yes") ? "1" : "0";
                    String maxTime = maxTimeTextField.getText();

                    // Validate that level, mines and maxtime can be parsed to integers (i.e they are integers and not some random text)
                    try {
                        Integer.parseInt(level);
                        Integer.parseInt(mines);
                        Integer.parseInt(maxTime);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter valid integers for Level, Mines, and Maximum Time.");
                        alert.showAndWait();
                        return null;
                    }
                    
                    if (id.isEmpty()) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter an ID for the scenario.");
                        alert.showAndWait();
                        return null;
                    } else {
                        return new String[] {id, level, mines, supermine, maxTime};
                    }
                }
                return null;
            });
        
            // Show the dialog and create the file if the Create button is clicked
            Optional<String[]> result = dialog.showAndWait();
            if (result.isPresent()) {
                String[] details = result.get();
                String id = details[0];
                String level = details[1];
                String mines = details[2];
                String supermine = details[3];
                String maxTime = details[4];
        
                // Create the scenario file
                try {
                    String fileName = "SCENARIO-" + id + ".txt";
                    String path = "medialab/" + fileName;
                    FileWriter writer = new FileWriter(path);
                    writer.write(level + "\n" + mines + "\n" + maxTime + "\n" + supermine);
                    writer.close();
                    System.out.println("Scenario created successfully.");
                } catch (IOException ex) {
                    System.err.println("Error creating scenario file.");
                    ex.printStackTrace();
                }
            }
        });

        /* 
         * If we select Load, a pop up window will appear asking for scenario id to load. 
         * In case of invalid id or data in the file, a pop up error window will appear
         * displaying a proper message. 
        */
        loadMenuItem.setOnAction(e -> {

            // Create a new dialog for the scenario ID
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Load scenario");
            dialog.setHeaderText("Please enter the ID of the scenario you want to load:");
            dialog.setContentText("Scenario ID:");
        
            // Get the scenario ID from the user
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String ID = result.get();
                ScenarioReader scenarioReader = new ScenarioReader();
                GameDescription gameDescription = new GameDescription();

                try {
                    // Read the scenario
                    gameDescription = scenarioReader.Read(ID);
                    // Update game's attributes. These will be used in create option.
                    setDifficultyLevel(gameDescription.getDifficultyLevel());
                    setNumMines(gameDescription.getNumMines());
                    setMaxTime(gameDescription.getMaxTime());
                    setHasSupermine(gameDescription.getHasSupermine());
                    Loaded = true;
                } catch (IOException | InvalidDescriptionException | InvalidValueException ex) {
                    setLoaded(false);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load scenario");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        /*
         * When start is selected, the scenario that was loaded last is used to start a game.
         * If no scenario is loaded, then an error message is displayed.
         */
        startMenuItem.setOnAction(e -> {
            
            if(getLoaded()) {
                Playing = true;
                play();
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to start a game");
                alert.setContentText("You must load a scenario first");
                alert.showAndWait();
            }
        });

        // If solution is selected, then gamelogic.Solution is called to reveal the solution 
        solutionMenuItem.setOnAction(e -> {
            if(Playing) {
                gameLogic.Solution();                
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to reveal solution");
                alert.setContentText("This option is unavailable when you are not playing. You must start a game first.");
                alert.showAndWait();
            }
        });

        roundsMenuItem.setOnAction(e -> {
            RoundsAction.handleRoundOption();
        });
        
        
        // Create a label with the text "Minesweeper" in silver
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(50));
        centerBox.setSpacing(20);
        centerBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel1 = new Label("Welcome to");
        titleLabel1.setTextFill(Color.SILVER);
        titleLabel1.setStyle("-fx-font-size: 58px; -fx-font-weight: bold;");

        Label TitleLabel2 = new Label("MediaLab Minesweeper");
        TitleLabel2.setTextFill(Color.SILVER);
        TitleLabel2.setStyle("-fx-font-size: 58px; -fx-font-weight: bold;");

        centerBox.getChildren().addAll(titleLabel1, TitleLabel2);

        // Add the components to the BorderPane
        this.setTop(menuBar);
        this.setCenter(centerBox);
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * This method initializes and starts the game. It generates a game board, sets up a timer,
     * and allows the user to interact with the game by left-clicking to reveal tiles and right-clicking to flag tiles.
     */
    public void play() {
        
        int boardsize = (getDifficultyLevel() == 1) ? 9 : 16;
        
        GridPane gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);
        gameBoard.setPadding(new Insets(10));
        gameBoard.setHgap(5);
        gameBoard.setVgap(5);
    
        BoardGenerator boardGenerator = new BoardGenerator(difficultyLevel, numMines, hasSupermine);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Check the gameOver flag
                Platform.runLater(() -> {
                    if(gameLogic.getGameOver()) {
                        GameOverAction(gameBoard);
                        timer.cancel();
                    }
                });
            }       
        };

        // Schedule the TimerTask to run every second.
        timer.scheduleAtFixedRate(task, 0, 1000);

        board = boardGenerator.GenerateBoard();
        boardGenerator.BoardPrinter();
        for (int row = 0; row < boardsize; row++) {
            for (int col = 0; col < boardsize; col++) {
                gameBoard.add(board[row][col], col, row);
            }
        }

        gameLogic = new GameLogic(board, numMines, maxTime);

        Label TotalMinesLabel = new Label("Total mines: " + numMines);
        Label MarkedTilesLabel = new Label("Marked tiles: " + gameLogic.getMarkedTiles());
        Label timeLeftLabel = new Label("Remaining time: " + gameLogic.getTimeLeft() + " secs");

        VBox infoBox = new VBox();
        infoBox.getChildren().addAll(TotalMinesLabel, MarkedTilesLabel, timeLeftLabel);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(50));

        Timer timer2 = new Timer();
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    TotalMinesLabel.setText("Total mines: " + numMines);
                    MarkedTilesLabel.setText("Marked tiles: " + gameLogic.getMarkedTiles());
                    timeLeftLabel.setText("Remaining time: " + gameLogic.getTimeLeft() + " secs");
                });
            }       
        };

        // Schedule the TimerTask to run every second.
        timer2.scheduleAtFixedRate(task2, 0, 1000);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(infoBox, gameBoard);
        this.setCenter(centerBox);

        for(int row = 0; row < boardsize; row++) {
            for (int col = 0; col < boardsize; col++) {
                int x = row;
                int y = col;
                gameLogic.getBoard()[row][col].setOnMouseClicked(event -> {
                    // Case -> Left Click
                    if (event.getButton() == MouseButton.PRIMARY) {
                        // If the first attempt is a mine or a supermine, change the board until it isn't.
                        while(gameLogic.getAttempts() == 0 && (board[x][y].getValue() == MINE || board[x][y].getValue() == SUPER_MINE)) {
                            play();
                        }
                        if(gameLogic.getAttempts() == 0) {
                            boardGenerator.MineRecorder();
                        }
                        gameLogic.AddRevealClick(x, y);
                    }
                    // Case -> Right Click
                    else if (event.getButton() == MouseButton.SECONDARY) {
                        gameLogic.flagTile(x, y);
                    }
                    board = gameLogic.getBoard();
                });
            }
        }
    }

    /**
     * Checks if the game is won or lost and displays the proper message
     * @param gameBoard the game board grid pane
     */
     private void GameOverAction(GridPane gameBoard) {
        
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(50));
        centerBox.setSpacing(20);
        centerBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));    

        Label label = new Label();

        if(gameLogic.getGameLost()) {
            label = new Label("You lost!");
            label.setTextFill(Color.RED);
        }
        else {
            label = new Label("You won!");
            label.setTextFill(Color.GREEN);
        }
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        
        // Add the components to the BorderPane
        centerBox.getChildren().addAll(label, gameBoard);
        this.setCenter(centerBox);

        Playing = false;
    } 

    // setters

    /**
     * Sets the difficulty level of the game
     * @param difficultyLevel the difficulty level of the game
     */
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Sets the number of mines in the game
     * @param numMines the number of mines in the game
     */
    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    /**
     * Sets the maximum time allowed to complete the game
     * @param maxTime the maximum time to complete the game
     */
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * Sets the HasSupermine flag which indicates whether the game contains a supermine
     * @param hasSupermine true if the game contains a supermine, false otherwise
     */
    public void setHasSupermine(boolean hasSupermine) {
        this.hasSupermine = hasSupermine;
    }

    /**
     * Sets the game board of tiles
     * @param board the game board of tiles
     */
    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    /**
     * Sets the Loaded flag which indicates whether the scenario has been loaded or not
     * @param Loaded true if the scenario has been loaded, false otherwise
     */
    public void setLoaded(boolean Loaded) {
        this.Loaded = Loaded;
    }

    // getters

    /**
     * Return the difficulty level of the game.
     * @return the difficulty level of the game
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Returns the number of mines in the game.
     * @return the number of mines in the game
     */
    public int getNumMines() {
        return numMines;
    }

    /**
     * Returns the maximum time allowed to complete the game
     * @return the maximum time allowed to complete the game
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * Returns the HasSupermine flag 
     * @return true if the game contains a supermine, false otherwise
     */
    public boolean getHasSupermine() {
        return hasSupermine;
    }

    /**
     * Returns the game's board
     * @return game's board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Returns the getLoaded flag
     * @return true if a scenario has been loaded, false otherwise
     */
    public boolean getLoaded() {
        return Loaded;
    }
}