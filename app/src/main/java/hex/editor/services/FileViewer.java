package hex.editor.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import java.util.*;

public class FileViewer {
    private Path path;


    public FileViewer(String path) {
        this.path = Paths.get(path);
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<String>();
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.toList();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return lines;
    }
}
