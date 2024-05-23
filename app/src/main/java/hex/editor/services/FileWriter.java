package hex.editor.services;

import hex.editor.model.CacheFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    public static void saveFile(Path filePath, List<List<String>> data) {
        while (FileViewer.isCaching()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < FileViewer.getSize(); i++){
            CacheFile cacheFile = FileViewer.getCurrentFile();
            writeInFile(new File(filePath.toUri()), cacheFile.getData());
            if (i != FileViewer.getSize() - 1) FileViewer.nextFile();
        }
        System.out.println("File saved!");
    }

    public static void writeCacheFile(CacheFile file) {
        writeInFile(new File(file.getPath().toUri()), file.getData());
    }

    private static void writeInFile(File file, List<List<String>> data) {
        try (FileOutputStream fos = new FileOutputStream(file); FileChannel fileChannel = fos.getChannel()) {
            for (List<String> line : data) {
                for (String hex : line) {
                    fileChannel.write(ByteBuffer.wrap(hex.getBytes(StandardCharsets.UTF_8)));
                    fileChannel.write(ByteBuffer.wrap(";".getBytes(StandardCharsets.UTF_8)));
                }
                fileChannel.write(ByteBuffer.wrap("\n".getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
    }
}