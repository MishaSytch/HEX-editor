package hex.editor.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import hex.editor.model.CacheLines;

public class FileViewer  {
    private static Integer countOfColumn = null;
    private static Integer countOfRow = null;
    private static long size;
    private static final Deque<String> queue = new ArrayDeque<>();
    private static final String CACHE_FILE = ".cache";
    private static File cacheFile;
    private static CacheLines cache;
    private static File file;
    private static long currentIndex = 0;
    private static long cachedIndex = 0;
    private static int part = 1;
    
    public static File getCacheFile() {
        return cacheFile;
    }

    public static long getCurrentIndex() {
        return currentIndex;
    }

    public static void openFile(String path, Integer countOfColumn, Integer countOfRow) {
        if (file != null) delete();
        file = new File(path);
        cacheFile = new File(CACHE_FILE); 
        FileViewer.countOfColumn = countOfColumn;
        FileViewer.countOfRow = (int) (countOfRow != null ? countOfRow : Math.min(file.length() / countOfColumn + 1, 20));
        size = FileViewer.countOfRow * countOfColumn;
    }
    
    public static void openFile(String path, Integer countOfColumn) {
        openFile(path, countOfColumn, null);
    }
    
    public static CacheLines getCurrentLines() throws IOException {
        if (cache != null && cache.getIndex() == currentIndex) return cache;
        return (cache = new CacheLines(getPartFile(file, currentIndex), currentIndex, part));
    }

    public static CacheLines getNextLines() throws IOException {
        currentIndex = Math.min(file.length() < size ? 0 : file.length() - size, currentIndex + size);
        if (!isLast()) {
            part++;
        }
        cachedIndex = Math.max(cachedIndex, currentIndex + size);
        return getCurrentLines();
    }

    public static CacheLines getPreviousLines() throws IOException {
        part = Math.max(1, part - 1);
        currentIndex = Math.max(0, currentIndex - size);
        return getCurrentLines();
    }

    public static CacheLines getFirstLines() throws IOException {
        currentIndex = 0;
        return getCurrentLines();
    }

    public static List<List<String>> getCacheLines() throws FileNotFoundException, IOException {
        List<List<String>> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(cacheFile, StandardCharsets.UTF_8.name())) {
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                List<String> data = Arrays.asList(line.split(";"));
                lines.add(data.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
            }
        }
        return lines;
    }

    public static List<List<String>> getFile() throws FileNotFoundException, IOException {
        List<List<String>> data = getCacheLines();
        data.addAll(getUncachedFile());
        return data;
    }

    public static boolean isLast() {
        return file.length() == currentIndex + size;
    }

    public static void delete() {
        cache = null;
        currentIndex = 0;
        file = null;
        cacheFile.delete();

        countOfColumn = null;
        countOfRow = null;
        size = 0;
        queue.clear();

        part = 1;
    }

    private static List<List<String>> getPartFile(File file, long from) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            long chunkSize = Math.min(size, fileChannel.size());
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, from, chunkSize);

            return readFromBuffer(buffer);
        }
    }

    private static List<List<String>> getUncachedFile() throws IOException {
        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, cachedIndex, file.length());
            List<List<String>> data = new ArrayList<>();
            do {
                data.addAll(readFromBuffer(buffer));
            } while (!queue.isEmpty());

            return data;
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

