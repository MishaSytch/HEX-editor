package hex.editor.services;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileWriterTest {
    String path = FilePaths.getTextFile();

    @Test
    void testGetRegexForSplit() {
        assertEquals("[\\t\\s\\W+]", FileWriter.getRegexForSplit());
    }

    @Test
    void testGetSeparator() {
        assertEquals(";", FileWriter.getSeparator());
    }

    @Test
    void testSaveFile() {
        FileViewer.openFile(path, 10);
        File file = new File(FilePaths.getSaveFile());
        assertDoesNotThrow(() -> FileWriter.saveFile(Paths.get((file).toURI())));
        file.deleteOnExit();
    }
}