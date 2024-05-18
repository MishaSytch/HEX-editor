package hex.editor.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileWriter {

    public static void writeFileFromListOfLists(Path filePath, List<List<String>> data) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath + ".txt"))) {
            for (List<String> lineData : data) {
                String line = String.join(";", lineData);
                writer.write(line);
                writer.newLine();
            }
        }
    }
}