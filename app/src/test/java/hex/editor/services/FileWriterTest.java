package hex.editor.services;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileWriterTest {
    String path = "C:/Users/misha/Documents/nicetu/HEX-editor/app/src/test/resources/text.txt";

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
        // Mocking necessary objects and methods
        FileViewer.openFile(path, 10);

        File file = new File("C:/Users/misha/Documents/nicetu/HEX-editor/app/src/test/resources/test/test.txt");
        // Testing the saveFile method
        assertDoesNotThrow(() -> FileWriter.saveFile(Paths.get((file).toURI())));
        file.deleteOnExit();
    }
}