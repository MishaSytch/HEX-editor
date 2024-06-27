package hex.editor.services;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheLines;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardCopyOption.*;

import static java.nio.file.LinkOption.*;

public class FileWriter {
    private static final String separator = ";";
    private static final String regex_for_split = "[\\t\\s\\W+]";

    public static String getRegexForSplit() {
        return regex_for_split;
    }

    public static String getSeparator() {
        return separator;
    }

    public static void saveFile(Path filePath) {
        writeInFile(new File(filePath.toUri()), null);
    }

    public static void writeInCacheFile(CacheLines cache) {
        writeInFile(FileViewer.getCacheFile(), cache);
    }

    private static void writeInFile(File file, CacheLines cache) {
        try {
            if (cache == null) {
                Path path = Files.copy(
                    FileViewer.getCurrentFile().toPath(),
                    file.toPath(),
                    REPLACE_EXISTING,
                    NOFOLLOW_LINKS
                );

                RandomAccessFile randomAccessFile = new RandomAccessFile((file = path.toFile()), "rw");
                randomAccessFile.seek(0);

                List<String> hex = FileViewer.getAllCachedLines();
                for (String item : hex) {
                    randomAccessFile.write(HexEditor.getCharFromHex(item).getBytes(StandardCharsets.UTF_8));    
                }

                randomAccessFile.close();

            } else {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(cache.getIndex());
                int count = 0;
                for (List<String> line : cache.getData()) {
                    for (String hex : line) {
                        count++;
                        randomAccessFile.write(hex != null ? hex.getBytes(StandardCharsets.UTF_8) : "".getBytes(StandardCharsets.UTF_8));
                        randomAccessFile.write(separator.getBytes(StandardCharsets.UTF_8));
                    }
                }
                
                randomAccessFile.close();
                FileViewer.cached(count);
            }
        } catch (Exception exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
    }
}