package hex.editor.services;

import org.junit.*;
import org.junit.jupiter.api.Test;


public class TestFileViewer {
    String bigFilePath = new String("app/src/test/filesForTest/bigText.txt"); 
    String verySmallFilePath = new String("app/src/test/filesForTest/verySmallText.txt");
    
    @Test
    void testFileViewer() {
        FileViewer fileViewer = new FileViewer(verySmallFilePath);

    }
}
