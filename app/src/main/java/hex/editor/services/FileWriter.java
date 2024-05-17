package hex.editor.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    public static void writeFileFromListOfLists(Path filePath, List<List<String>> data) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (List<String> lineData : data) {
                String line = String.join(",", lineData); // Joining each string in the sublist with a comma
                writer.write(line);
                writer.newLine(); // Writing a new line after each list
            }
        }
    }
}