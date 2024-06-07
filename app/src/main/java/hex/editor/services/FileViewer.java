package hex.editor.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
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
    private static final Deque<String> queue = new ArrayDeque<>();
    private static File cacheFile;
    private static CacheLines cache;
    
    private static int LENGTH_OF_LINE;
    private static final String CACHE_FILE = ".cache";
    private static File FILE;
    private static long CURRENT_CHAR_IN_FILE = 0;
    private static long CACHED_CHAR = 0;
    private static int PART = 1;
    
    public static File getCacheFile() {
        return cacheFile;
    }

    public static long getCURRENT_CHAR_IN_FILE() {
        return CURRENT_CHAR_IN_FILE;
    }

    public static void openFile(String path, Integer countOfColumn, Integer countOfRow) {
        if (FILE != null) delete();
        FILE = new File(path);
        cacheFile = new File(CACHE_FILE); 
        FileViewer.countOfColumn = countOfColumn;
        FileViewer.countOfRow = (int) (countOfRow != null ? countOfRow : Math.min(FILE.length() / countOfColumn + 1, 20));
        LENGTH_OF_LINE = FileViewer.countOfRow * countOfColumn;
    }
    
    public static void openFile(String path, Integer countOfColumn) {
        openFile(path, countOfColumn, null);
    }
    
    public static CacheLines getCurrentLines() throws IOException {
        if (cache != null && cache.getPart() == PART) return cache;
        return (cache = new CacheLines(readFile(FILE, CURRENT_CHAR_IN_FILE, LENGTH_OF_LINE), PART));
    }

    public static CacheLines getNextLines() throws IOException {
        CURRENT_CHAR_IN_FILE = Math.min(FILE.length(), CURRENT_CHAR_IN_FILE + LENGTH_OF_LINE);
        if (!isLast()) {
            PART++;
        }
        if (cacheFile.length() > cache.getNextIndex()) return (cache = new CacheLines(getNextCachedLine(), PART));

        return getCurrentLines();
    }

    public static CacheLines getPreviousLines() throws IOException {
        PART = Math.max(1, PART - 1);
        CURRENT_CHAR_IN_FILE = Math.max(0, CURRENT_CHAR_IN_FILE - LENGTH_OF_LINE);

        return (cache = new CacheLines(getPreviousCachedLine(), PART));
    }
    
    public static List<List<String>> getFile() throws FileNotFoundException, IOException {
        List<List<String>> data = getAllCachedLines();
        data.get(0).addAll(getUncachedFile());
        return data;
    }
    
    public static boolean isLast() {
        return FILE.length() == CURRENT_CHAR_IN_FILE + LENGTH_OF_LINE;
    }
    
    public static void delete() {
        cache = null;
        CURRENT_CHAR_IN_FILE = 0;
        FILE = null;
        if (cacheFile != null || (cacheFile = new File(CACHE_FILE)).exists()) cacheFile.delete();
        
        countOfColumn = null;
        countOfRow = null;
        LENGTH_OF_LINE = 0;
        queue.clear();
        
        PART = 1;
    }

    public static void cached(int size) {
        if (isLast()) return;

        CACHED_CHAR = Math.max(CACHED_CHAR, CURRENT_CHAR_IN_FILE + size);
    }

    private static List<List<String>> getNextCachedLine() throws IOException {
        return readCacheFile(cache.getNextIndex());
    }

    private static List<List<String>> getPreviousCachedLine() throws IOException {
        return readCacheFile(cache.getPreviousIndex());
    }
    
    private static List<List<String>> getAllCachedLines() throws FileNotFoundException, IOException {
        List<List<String>> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(cacheFile, StandardCharsets.UTF_8.name())) {
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                List<String> data = Arrays.asList(line.split(FileWriter.getSeparator()));
                lines.add(data.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
                }
        }

        return lines;
    }
    
    private static List<List<String>> readCacheFile(long from) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(CACHE_FILE, "r")) {
            byte[] bytes = new byte[cache.getLength()];
            randomAccessFile.seek(from);
            randomAccessFile.read(bytes);

            String[] line = (new String(bytes, StandardCharsets.UTF_8)).split(FileWriter.getSeparator());
            int indexOfChar = 0;

            List<List<String>> outer = new ArrayList<>();

            for (int row = 0 ; row < countOfRow; row++) {
                List<String> inner = new ArrayList<>();
                for (int column = 0; column < countOfColumn; column++) {
                    inner.add(line[indexOfChar++]);
                }
                outer.add(inner);
            }

            return outer;
        }
    }
    
    private static List<List<String>> readFile(File file, long from, int size) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILE, "r")) {
            byte[] bytes = new byte[size];
            randomAccessFile.seek(from);
            randomAccessFile.read(bytes);

            String line = new String(bytes, StandardCharsets.UTF_8);

            return fromLineToMatrix(line);
        }
    }

    private static List<String> getUncachedFile() throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILE, "r")) {
            byte[] bytes = new byte[(int)(FILE.length() - CACHED_CHAR)];
            randomAccessFile.seek(CACHED_CHAR);
            randomAccessFile.read(bytes);
            
            List<String> data = new ArrayList<>();
            String line = new String(bytes, StandardCharsets.UTF_8);
            HexService.getHexFromString(line).forEach(data::add);

            return data;
        }
    }

    private static List<List<String>> fromLineToMatrix(String line) {
        List<List<String>> lines = new ArrayList<>();
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

