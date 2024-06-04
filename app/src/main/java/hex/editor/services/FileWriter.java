package hex.editor.services;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheLines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    public static void saveFile(Path filePath) {
        writeInFile(new File(filePath.toUri()), null);
        System.out.println("File saved!");
    }

    public static void writeInCacheFile(CacheLines cache) {
        writeInFile(FileViewer.getCacheFile(), cache);
    }

    private static void writeInFile(File file, CacheLines cache) {
        try (FileOutputStream fos = new FileOutputStream(file); FileChannel fileChannel = fos.getChannel()) {
            if (cache == null) {
                List<List<String>> hex = FileViewer.getCacheLines();
                for (List<String> row : hex) {
                    for (String item : row) {
                        fileChannel.write(ByteBuffer.wrap((HexEditor.getCharFromHex(item)).getBytes(StandardCharsets.UTF_8)));
                    }
                }
                FileViewer.getNextLines();
            } else {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(cache.getIndex());
                for (List<String> line : cache.getData()) {
                    for (String hex : line) {
                        randomAccessFile.write(hex != null ? hex.getBytes(StandardCharsets.UTF_8) : " ".getBytes(StandardCharsets.UTF_8));
                        randomAccessFile.write(";".getBytes(StandardCharsets.UTF_8));

                    }
                    randomAccessFile.write("\n".getBytes(StandardCharsets.UTF_8));
                }
                randomAccessFile.close();
            }
        } catch (IOException exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
    }
}