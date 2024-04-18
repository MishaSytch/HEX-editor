package hex.editor.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;

public class TestFileViewer {
    static String bigFilePath = FilePaths.getBigFilePath();; 
    static String verySmallFilePath = FilePaths.getVerySmallFilePath();
    
    @Test
    void testFile() {
        File file1 = new File(verySmallFilePath);
        File file2 = new File(bigFilePath);

        Assertions.assertTrue(file1.exists());
        Assertions.assertTrue(file1.isFile());
        Assertions.assertTrue(file2.exists());
        Assertions.assertTrue(file2.isFile());
    }

    @Test
    void testFileViewer() {
        FileViewer fileViewer = new FileViewer(verySmallFilePath);
        List<String> lines = fileViewer.getLines();
        List<String> list = new ArrayList<String>();
        list.add("Pulvinar elementum integer.");
        list.add("");
        list.add("Заморозки наступили 3-го числа!");
        Assertions.assertEquals(lines, list);
    }
}
