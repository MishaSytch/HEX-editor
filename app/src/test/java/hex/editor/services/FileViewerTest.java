package hex.editor.services;

import hex.editor.model.CacheLines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileViewerTest {
    String path;
    Integer countOfColumn;
    Integer countOfRow;

    @BeforeEach
    void setUp() {
        path = "app/src/test/resources/text.txt";
        countOfColumn = 10;
        countOfRow = 5;
    }

    @Test
    public void testOpenFileWithCountOfColumnAndCountOfRow() {
        // When
        FileViewer.openFile(path, countOfColumn, countOfRow);

        // Then
        assertNotNull(FileViewer.getCacheFile());
    }

    @Test
    public void testOpenFileWithCountOfColumnOnly() {
        // When
        FileViewer.openFile(path, countOfColumn);

        // Then
        assertNotNull(FileViewer.getCacheFile());
    }

    @Test
    public void testGetCurrentLines() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);

        // When
        assertDoesNotThrow(FileViewer::getCurrentLines);
        CacheLines currentLines;
        try {
            currentLines = FileViewer.getCurrentLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertNotNull(currentLines);
    }

    @Test
    public void testGetNextLines() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);

        // When
        assertDoesNotThrow(FileViewer::getNextLines);
        CacheLines nextLines;
        try {
            nextLines = FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertNotNull(nextLines);
    }

    @Test
    public void testGetPreviousLines() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);


        // When
        try {
            FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(FileNotFoundException.class, FileViewer::getPreviousLines);
    }

    @Test
    public void testGetFile() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);
        try {
            FileViewer.getNextLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // When
        assertThrows(FileNotFoundException.class, FileViewer::getPreviousLines);
    }

    @Test
    public void testIsLast() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);

        // When
        boolean isLast = FileViewer.isLast();

        // Then
        assertFalse(isLast);
    }

    @Test
    public void testDelete() {
        // Given
        FileViewer.openFile(path, countOfColumn, countOfRow);

        // When
        FileViewer.delete();

        // Then
        assertNull(FileViewer.getCacheFile());
        assertNull(FileViewer.getCurrentFile());
    }
}