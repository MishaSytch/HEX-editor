package hex.editor.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import hex.editor.controller.HexEditor;

public class TestFileViewer {
    File[] files;

    // @BeforeAll
    void init() {
        File dir = new File("/home/msych/filesForTest/");
        files = dir.listFiles();
    }

    @Test 
    void openFile_first_Test() throws IOException {
        File dir = new File("/home/msych/filesForTest/");
        files = dir.listFiles();
        FileViewer.openFile(files[0].getAbsolutePath(), new Integer(10));
        assertEquals(0, FileViewer.getCurrentIndex());
        FileViewer.getCurrentLines();
        assertEquals(HexEditor.getHexFromChar("B"), FileViewer.getCurrentLines().getData().get(0).get(0));
        assertEquals(HexEditor.getHexFromChar("B"), FileViewer.getNextLines().getData().get(0).get(0));
        assertEquals(HexEditor.getHexFromChar("B"), FileViewer.getPreviousLines().getData().get(0).get(0));
    }

    @Test 
    void openFile_second_Test() throws IOException {
        File dir = new File("/home/msych/filesForTest/");
        files = dir.listFiles();
        for (File file : files) {
            FileViewer.openFile(file.getAbsolutePath(), new Integer((int) file.length()));
            assertEquals(0, FileViewer.getCurrentIndex());
            
            List<List<String>> res = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    res.add(Arrays.stream(line.split("")).map(s -> HexEditor.getHexFromChar(s)).collect(Collectors.toList()));
                }
            }
    
            assertEquals(res, FileViewer.getCurrentLines().getData());
            assertEquals(res, FileViewer.getNextLines().getData());
            assertEquals(res, FileViewer.getPreviousLines().getData());
        }
    }
}

