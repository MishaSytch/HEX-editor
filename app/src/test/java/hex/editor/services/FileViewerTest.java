package hex.editor.services;

import hex.editor.model.CacheLines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileViewerTest {
    String path;
    Integer countOfColumn;
    Integer countOfRow;

    @BeforeEach
    void setUp() {
        path = FilePaths.getTextFile();
        countOfColumn = 10;
        countOfRow = 5;
    }

    @Test
    public void testOpenFileWithCountOfColumnAndCountOfRow() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        assertNotNull(FileViewer.getCacheFile());
    }

    @Test
    public void testOpenFileWithCountOfColumnOnly() {
        FileViewer.openFile(path, countOfColumn);

        assertNotNull(FileViewer.getCacheFile());
    }

    @Test
    public void testGetCurrentLines() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        assertDoesNotThrow(FileViewer::getCurrentLines);
        CacheLines currentLines;
        try {
            currentLines = FileViewer.getCurrentLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(currentLines);
    }

    @Test
    public void testGetNextLines() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        assertDoesNotThrow(FileViewer::getNextLines);
        CacheLines nextLines;
        try {
            nextLines = FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(nextLines);
    }

    @Test
    public void testGetPreviousLines() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        try {
            FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(FileNotFoundException.class, FileViewer::getPreviousLines);
    }

    @Test
    public void testGetFile() {
        FileViewer.openFile(path, countOfColumn, countOfRow);
        try {
            FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(FileNotFoundException.class, FileViewer::getPreviousLines);
    }

    @Test
    public void testIsLast() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        boolean isLast = FileViewer.isLast();

        assertFalse(isLast);
    }

    @Test
    public void testDelete() {
        FileViewer.openFile(path, countOfColumn, countOfRow);

        FileViewer.delete();

        assertNull(FileViewer.getCacheFile());
        assertNull(FileViewer.getCurrentFile());
    }
}