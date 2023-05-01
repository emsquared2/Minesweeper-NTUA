package application.GameObjects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class handles file management for the game's rounds data.
 * It creates and writes data to text files in the "rounds" directory.
 * Each round's data includes the total number of mines, number of attempts, maximum time available, and winner.
 * If there are less than five files, it creates a new one and writes the data to it. If there are five files,
 * it finds the oldest file based on the last modified timestamp and overwrites it with the latest round's data.
*/
public class FileManager {

    private static final int MAX_ROUNDS = 5;
    private static final String FILE_PREFIX = "round-";
    private static final String FILE_EXTENSION = ".txt";

    /**
     * Creates the "rounds" directory if it does not exist.
     * If there are less than five files, it creates a new one and writes the current round's data to it.
     * If there are five files, it finds the oldest file based on the last modified timestamp and overwrites it
     * with the latest round's data.
     *
     * @param numMines      the total number of mines in the current round
     * @param attempts      the number of attempts (left clicks) in the current round
     * @param totalGameTime the total game time in seconds for the current round
     * @param winner        the winner of the current round
     * @throws IOException if an I/O error occurs
     */
    public static void writeCurrentRound(int numMines, int attempts, long totalGameTime, String winner) throws IOException {

        // Create a directory named "rounds" (in case it doesn't exist)
        Path roundsDirPath = Paths.get("rounds");
        if (!Files.exists(roundsDirPath)) {
            Files.createDirectory(roundsDirPath);
        }

        // Obtain each file under folder "rounds" and sort them according to the last modified timestamp
        File[] roundFiles = roundsDirPath.toFile().listFiles((dir, name) -> name.startsWith(FILE_PREFIX) && name.endsWith(FILE_EXTENSION));
        List<File> sortedFiles = Arrays.asList(roundFiles);
        sortedFiles.sort(Comparator.comparing(File::lastModified));

        // If there are less than 5 files, then create a new one.
        if (sortedFiles.size() < MAX_ROUNDS) {
            int newFileId = sortedFiles.size() + 1;
            String newFileName = FILE_PREFIX + newFileId + FILE_EXTENSION;
            Path newFilePath = roundsDirPath.resolve(newFileName);
            writeRoundFile(newFilePath, numMines, attempts, totalGameTime, winner);
        } else {
            // Otherwise, find the oldest one and overwrite it.
            File oldestFile = sortedFiles.get(0);
            writeRoundFile(oldestFile.toPath(), numMines, attempts, totalGameTime, winner);
        }
    }

    /**
     * Overwrites the file at the given path with the current round's data.
     *
     * @param FilePath      the path of the file to write the data to
     * @param numMines      the total number of mines in the current round
     * @param attempts      the number of attempts (left clicks) in the current round
     * @param totalGameTime the total game time in seconds for the current round
     * @param winner        the winner of the current round
     * @throws IOException if an I/O error occurs
     */
    private static void writeRoundFile(Path FilePath, int numMines, int attempts, long totalGameTime, String winner) throws IOException {
        try (FileWriter writer = new FileWriter(FilePath.toFile())) {
            writer.write("Total number of mines: " + numMines + ".\n");
            writer.write("Total number of attempts (left clicks): " + attempts + ".\n");
            writer.write("Total game time: " + totalGameTime + " secs.\n");
            writer.write("Winner: " + winner + ".\n");
        }
    }

}
