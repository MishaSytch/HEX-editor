package hex.editor.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;

public class TestFileViewer {
    static String bigFilePath = FilePaths.getBigFilePath();; 
    static String verySmallFilePath = FilePaths.getVerySmallFilePath();
    static String oneCharFilePath = FilePaths.getOneChar();
    
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
    void testFileViewer_verySmallFilePath() {
        FileViewer fileViewer = new FileViewer("C:\\Users\\misha\\Documents\\nicetu\\HEX-editor\\app\\src\\test\\java\\hex\\editor\\filesForTest\\verySmallText.txt");
        List<String> lines = fileViewer.getLines();
        List<String> list = new ArrayList<String>();
        list.add("Pulvinar elementum integer.");
        list.add("");
        list.add("Заморозки наступили 3-го числа!");
        Assertions.assertEquals(lines, list);
    }

    @Test
    void testFileViewer_oneCharFilePath() {
        FileViewer fileViewer = new FileViewer(oneCharFilePath);
        List<String> lines = fileViewer.getLines();
        List<String> list = new ArrayList<String>();
        list.add(new String(("B".getBytes(StandardCharsets.UTF_8))));
        Assertions.assertEquals(lines, list);
    }
}
