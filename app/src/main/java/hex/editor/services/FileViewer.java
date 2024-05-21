package hex.editor.services;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class FileViewer {
    private static final long CACHE_SIZE = 1024;
    private static final String CACHE_DIR = "cache";
    private static final List<File> cacheFiles = new LinkedList<>();
    private static int index = 0;
    private static Integer countOfColumn = null;
    private static Integer countOfRow = null;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition canAccessCacheFiles = lock.newCondition();
    private static boolean isCacheFilesBeingAccessed = false;


    public static void openFile(String path, Integer countOfColumn, Integer countOfRow) {
        File file = new File(path);
        FileViewer.countOfColumn = countOfColumn;
        FileViewer.countOfRow = countOfRow;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> cacheFuture = CompletableFuture.runAsync(() -> {
            try {
                cacheFile(file);
            } catch (IOException e) {
                System.err.println("Error caching file: " + e.getMessage());
            }
        }, executor);

        cacheFuture.thenRun(() -> {
            System.out.println("Cache complete");
        }).exceptionally(e -> {
            System.err.println("Error during caching: " + e.getMessage());
            return null;
        });


        executor.shutdown();
    }

    public static void openFile(String path, Integer countOfColumn) {
        openFile(path, countOfColumn, null);
    }

    public static List<List<String>> getCurrentLines() {
        List<List<String>> lines = new ArrayList<>();
        lock.lock();
        try {
            while (isCacheFilesBeingAccessed) {
                canAccessCacheFiles.await();
            }
            isCacheFilesBeingAccessed = true;
            while (cacheFiles.isEmpty()) {
            }

            File currentFile = cacheFiles.get(index);

            try (Scanner scanner = new Scanner(currentFile, StandardCharsets.UTF_8.name())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    List<String> data = Arrays.asList(line.split(";"));
                    lines.add(data.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                return Collections.emptyList();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            isCacheFilesBeingAccessed = false;
            canAccessCacheFiles.signalAll();
            lock.unlock();
        }

        return lines;
    }

    public static void nextLines() {
        index = Math.min(index + 1, cacheFiles.size() - 1);
    }


    public static void previousLines() {
        index = Math.max(index - 1, 0);
    }

    private static void cacheFile(File file) throws IOException {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            long position = 0;
            long size = fileChannel.size();
            while (position < size) {
                long remaining = size - position;
                long chunkSize = Math.min(remaining, CACHE_SIZE);
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, chunkSize);
                File chunkFile = new File(cacheDir, index++ + "." + file.getName());
                lock.lock();
                try {
                    while (isCacheFilesBeingAccessed) {
                        canAccessCacheFiles.await();
                    }
                    isCacheFilesBeingAccessed = true;
                    cacheFiles.add(chunkFile);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    isCacheFilesBeingAccessed = false;
                    canAccessCacheFiles.signalAll();
                    lock.unlock();
                }
                try (FileOutputStream fos = new FileOutputStream(chunkFile); FileChannel chunkChannel = fos.getChannel()) {
                    List<List<String>> lines = new ArrayList<>();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (buffer.hasRemaining()) {
                        stringBuilder.append((char) buffer.get());
                    }
                    String line = stringBuilder.toString();

                    Deque<String> queue = new ArrayDeque<>();
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
                position += chunkSize;
            }
        }
    }
}

