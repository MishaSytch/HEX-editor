package hex.editor.services;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    public static void saveFile(Path filePath) {
        while (FileViewer.isCaching()) { }

        writeInFile(new File(filePath.toUri()), null,  false);
        System.out.println("File saved!");
    }

    public static void writeCacheFile(CacheFile file) {
        writeInFile(new File(file.getPath().toUri()), file.getData(), true);
    }

    private static void writeInFile(File file, List<List<String>> data, boolean isCache) {
        try (FileOutputStream fos = new FileOutputStream(file); FileChannel fileChannel = fos.getChannel()) {
            if (!isCache) {
                for (int i = 0; i < FileViewer.getSize(); i++) {
                    List<List<String>> hex = FileViewer.getCurrentFile().getData();
                    for (List<String> row : hex) {
                        for (String item : row) {
                            fileChannel.write(ByteBuffer.wrap((HexEditor.getCharFromHex(item)).getBytes(StandardCharsets.UTF_8)));
                        }
                    }
                    if (i < FileViewer.getSize() - 1) FileViewer.nextFile();
                }
            } else {
                if (data == null) throw new NullPointerException();

                for (List<String> line : data) {
                    for (String hex : line) {
                        fileChannel.write(ByteBuffer.wrap(hex.getBytes(StandardCharsets.UTF_8)));
                        fileChannel.write(ByteBuffer.wrap(";".getBytes(StandardCharsets.UTF_8)));

                    }
                    fileChannel.write(ByteBuffer.wrap("\n".getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (IOException exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
    }
}