package hex.editor.services;

import hex.editor.model.CacheFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.stream.Collectors;

public class FileViewer  {
    private static Integer countOfColumn = null;
    private static Integer countOfRow = null;
    private static final Deque<String> queue = new ArrayDeque<>();
    private static final String CACHE_DIR = ".cache";
    private static File file;
    private static long currentIndex = 0;

    public static void openFile(String path, Integer countOfColumn, Integer countOfRow) {
        file = new File(path);
        FileViewer.countOfColumn = countOfColumn;
        FileViewer.countOfRow = (int) (countOfRow != null ? countOfRow : Math.min(file.length() / countOfColumn, 20));
    }
    
    public static void openFile(String path, Integer countOfColumn) {
        openFile(path, countOfColumn, null);
    }
    
    public static List<List<String>> getCurrentLines() throws IOException {
        return partFile(file, currentIndex);
    }

    public static List<List<String>> getNextLines() throws IOException {
        currentIndex = Math.min(file.length(), currentIndex + countOfColumn * countOfRow);
        return getCurrentLines();
    }

    public static List<List<String>> getPreviousLines() throws IOException {
        currentIndex = Math.max(0, currentIndex - countOfColumn * countOfRow);
        return getCurrentLines();
    }

    public static List<List<String>> getFirstLines() throws IOException {
        currentIndex = 0;
        return getCurrentLines();
    }

    public static boolean isLast() {
        return file.length() == currentIndex;
    }

    private static List<List<String>> partFile(File file, long from) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            long size = fileChannel.size();
            long remaining = size - from;
            long chunkSize = Math.min(remaining, size);
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, from, chunkSize);

            return readFromBuffer(buffer);
        }
    }

    private static List<List<String>> readFromBuffer(MappedByteBuffer buffer) {
        List<List<String>> lines = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        while (buffer.hasRemaining()) {
            stringBuilder.append((char) buffer.get());
        }
        String line = stringBuilder.toString();

        int rows = 0;
        HexService.getHexFromString(line).forEach(queue::addLast);
        List<String> row = new LinkedList<>();
        while (!queue.isEmpty()) {
            row.add(queue.removeFirst());
            if (row.size() == countOfColumn) {
                lines.add(row);
                row = new LinkedList<>();
                if (countOfRow != null) {
                    rows++;
                    if (rows == countOfRow) break;
                }
                continue;
            }
            if (queue.isEmpty()) {
                lines.add(row);
            }
        }
        return lines;
    }
}

