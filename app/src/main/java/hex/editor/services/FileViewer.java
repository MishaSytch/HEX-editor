package hex.editor.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileViewer {
    private String path;


    public FileViewer(String path) {
        this.path = path;
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return lines;
    }
}
