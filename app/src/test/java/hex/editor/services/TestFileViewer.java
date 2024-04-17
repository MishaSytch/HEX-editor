package hex.editor.services;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;

public class TestFileViewer {
    static String bigFilePath; 
    static String verySmallFilePath;

    @BeforeAll
    static void getter() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
    }
    
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
        list.add("Pulvinar elementum integer enim neque volutpat ac tincidunt vitae. ");
        list.add("Felis imperdiet proin fermentum leo vel orci. Mauris in aliquam sem fringilla ut morbi. Quam vulputate dignissim suspendisse in est. ");
        list.add("Eget aliquet nibh praesent tristique magna sit amet purus gravida. Ipsum dolor sit amet consectetur adipiscing elit duis. Fringilla est ullamcorper eget nulla facilisi. In hendrerit gravida rutrum quisque non tellus orci ac. Risus nullam eget felis eget nunc. Ut faucibus pulvinar elementum integer enim neque volutpat. Pretium fusce id velit ut. Tristique nulla aliquet enim tortor at auctor urna. ");
        list.add("");
        list.add("Cras tincidunt lobortis feugiat vivamus at augue eget.");
        Assertions.assertEquals(lines, list);
    }
}
