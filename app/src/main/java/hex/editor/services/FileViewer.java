package hex.editor.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileViewer {

    public static List<List<String>> getLines(String path, int countOfColumn) {
        List<List<String>> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            Deque<String> queue = new ArrayDeque<>();
            while ((line = reader.readLine()) != null) {
                HexService.getHexFromString(line).forEach(queue::addLast);
                if (queue.size() >= countOfColumn) {
                    List<String> row = new LinkedList<>();
                    while (!queue.isEmpty()) {
                        row.add(queue.removeFirst());
                        if (row.size() == countOfColumn) {
                            lines.add(row);
                            row = new LinkedList<>();
                        }
                    }
                }
            }
            List<String> row = new LinkedList<>();
            while (!queue.isEmpty()) {
                row.add(queue.removeFirst());
            }
            lines.add(row);
        } catch (IOException exception) {
            // Consider logging the exception or rethrowing it as a custom checked exception
            System.err.println("Error processing file: " + exception.getMessage());
            return Collections.emptyList(); // Return an empty list or null depending on how you want to handle errors
        }
        System.out.println("File road");
        return lines;
    }
}
