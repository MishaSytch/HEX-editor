package hex.editor.services;

import hex.editor.model.CacheFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    public static void saveFile(Path filePath, List<List<String>> data) {
        if (FileViewer.getCurrentFile().isModified()) writeCacheFile(data, FileViewer.getCurrentFile());
        while (FileViewer.isCaching()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int i = 0; i < FileViewer.getSize(); i++){
                CacheFile cacheFile = FileViewer.getCurrentFile();
                writeInFile(cacheFile.getData(), writer);
                if (i != FileViewer.getSize() - 1) FileViewer.nextFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("File saved!");
    }

    public static void writeCacheFile(List<List<String>> data, CacheFile file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.getPath())) {
            writeInFile(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeInFile(List<List<String>> data, BufferedWriter writer) {
        for (List<String> lineData : data) {
            String line = String.join(";", lineData);
            try {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}