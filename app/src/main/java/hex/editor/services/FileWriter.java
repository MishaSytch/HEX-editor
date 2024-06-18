package hex.editor.services;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheLines;

import java.io.File;
import java.io.IOException;
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
        int SIZE_LINE = (int) Math.pow(2, 3 * 5);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            if (cache == null) {
                Files.copy(
                    FileViewer.getCurrentFile().toPath(),
                    file.toPath(),
                    REPLACE_EXISTING,
                    NOFOLLOW_LINKS
                );
                randomAccessFile.seek(0);

                List<List<String>> hex = FileViewer.getAllCachedLines();
                StringBuilder sb = new StringBuilder();
                for (List<String> row : hex) {
                    for (String item : row) {
                        if (sb.length() == SIZE_LINE) {
                            randomAccessFile.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                            sb = new StringBuilder();
                        }
                        sb.append(HexEditor.getCharFromHex(item));
                    }
                }

                randomAccessFile.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                
            } else {
                randomAccessFile.seek(cache.getIndex());
                int count = 0;
                for (List<String> line : cache.getData()) {
                    for (String hex : line) {
                        count++;
                        randomAccessFile.write(hex != null ? hex.getBytes(StandardCharsets.UTF_8) : "".getBytes(StandardCharsets.UTF_8));
                        randomAccessFile.write(separator.getBytes(StandardCharsets.UTF_8));
                    }
                }
                
                FileViewer.cached(count);
            }
        } catch (IOException exception) {
            System.err.println("Error processing file: " + exception.getMessage());
        }
    }
}