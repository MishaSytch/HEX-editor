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

public class FileViewer {
    private static Integer countOfColumn = null;
    private static Integer countOfRow = null;
    private static final Deque<String> queue = new ArrayDeque<>();
    private static final long CACHE_SIZE = 10 * 1024;
    private static final String CACHE_DIR = "cache";
    private static final List<File> cacheFiles = new LinkedList<>();
    private static int index = 0;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition cacheFilesReading = lock.newCondition();
    private static Thread cacheThread;
    private static long lastRowNumber = 0;
    public static long maxRowNumberStarts() {
        return lastRowNumber;
    }

    public static void openFile(String path, Integer countOfColumn, Integer countOfRow) {
        File file = new File(path);
        FileViewer.countOfColumn = countOfColumn;
        FileViewer.countOfRow = countOfRow;
        cacheFiles.clear();
        lastRowNumber = 0;

        cacheThread = new Thread(() -> {
            try {
                cacheFile(file);
            } catch (IOException e) {
                System.err.println("Error caching file: " + e.getMessage());
            }
        });
        cacheThread.start();
    }

    public static void openFile(String path, Integer countOfColumn) {
        openFile(path, countOfColumn, null);
    }

    public static CacheFile getCurrentFile() {
        long firstRowNumber = 0;
        List<List<String>> lines = new ArrayList<>();
        Path path;
        try {
            lock.lock();
            while (cacheFiles.isEmpty()) {
                cacheFilesReading.await();
            }

            File currentFile = cacheFiles.get(index);
            path = Paths.get(currentFile.getAbsolutePath());
            try {
                firstRowNumber = Long.parseLong(currentFile.getName().split("\\.")[1]) ;
            } catch (NumberFormatException ignored) {
            }

            try (Scanner scanner = new Scanner(currentFile, StandardCharsets.UTF_8.name())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    List<String> data = Arrays.asList(line.split(";"));
                    lines.add(data.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        return new CacheFile(firstRowNumber, lines, path, index);
    }

    public static void nextFile() {
        index = Math.min(index + 1, cacheFiles.size() - 1);
    }


    public static void previousFile() {
        index = Math.max(index - 1, 0);
    }
    
    public static int getSize() {
        return cacheFiles.size();
    }
    
    public static boolean isCaching() {
        return cacheThread.isAlive();
    }
    
    public static void removeCache() {
        File cacheDir = new File(CACHE_DIR);
        boolean res = cacheDir.delete();
        System.out.println("Cache deleted with " + res);
        index = 0;
        lastRowNumber = 0;
    }

    private static void cacheFile(File file) throws IOException {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            long firstRowNumber = 0;
            long position = 0;
            long size = fileChannel.size();
            while (position < size) {
                long remaining = size - position;
                long chunkSize = Math.min(remaining, CACHE_SIZE);
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, chunkSize);
                List<List<String>> lines = readFromBuffer(buffer);
                File chunkFile = new File(cacheDir,  Math.abs(file.hashCode()) + "." + firstRowNumber);
                lastRowNumber = Math.max(lastRowNumber, firstRowNumber);
                firstRowNumber += lines.size();
                try (FileOutputStream fos = new FileOutputStream(chunkFile); FileChannel chunkChannel = fos.getChannel()) {
                    for (List<String> data : lines) {
                        for (String hex : data) {
                            chunkChannel.write(ByteBuffer.wrap(hex.getBytes(StandardCharsets.UTF_8)));
                            chunkChannel.write(ByteBuffer.wrap(";".getBytes(StandardCharsets.UTF_8)));
                        }
                        chunkChannel.write(ByteBuffer.wrap("\n".getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException exception) {
                    System.err.println("Error processing file: " + exception.getMessage());
                }
                try {
                    Thread.sleep(0,10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    lock.lock();
                    cacheFiles.add(chunkFile);
                    cacheFilesReading.signalAll();
                }  finally {
                    lock.unlock();
                }
                position += chunkSize;
            }
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

