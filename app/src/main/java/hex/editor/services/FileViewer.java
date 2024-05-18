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

    public static List<List<String>> getLines(String path, int countOfColumn, int countOfRow) {
        List<List<String>> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            Deque<String> queue = new ArrayDeque<>();
            int rows = 0;
            exit: while ((line = reader.readLine()) != null) {
                HexService.getHexFromString(line).forEach(queue::addLast);
                if (queue.size() >= countOfColumn) {
                    List<String> row = new LinkedList<>();
                    while (!queue.isEmpty()) {
                        row.add(queue.removeFirst());
                        if (row.size() == countOfColumn) {
                            lines.add(row);
                            row = new LinkedList<>();
                            rows++;
                            if (rows == countOfRow) break exit;
                        }
                    }
                }
            }
            List<String> row = new LinkedList<>();
            if (rows != countOfRow) {
                while (!queue.isEmpty()) {
                    row.add(queue.removeFirst());
                }
                lines.add(row);
            }
        } catch (IOException exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
        System.out.println("File road");
        return lines;
    }
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
            System.err.println("Error processing file: " + exception.getMessage());
        }
        System.out.println("File road");
        return lines;
    }
}
