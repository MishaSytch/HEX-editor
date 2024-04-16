package hex.editor;

import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.*;

import com.google.common.base.Strings;

import hex.editor.services.FileViewer;
import hex.editor.services.HEXservice;

public class AppTest {
    String bigFilePath = new String("app/src/test/filesForTest/bigText.txt"); 
    String verySmallFilePath = new String("app/src/test/filesForTest/verySmallText.txt");

    @AfterAll
    void appExecuted() {
        App app = new App();
        Assertions.assertNotNull(app);

        HexEditor hex = new HexEditor();
        Assertions.assertNotNull(hex);
    }


    @Test
    void HEXserviceTest() {
        
        HEXservice heXservice = new HEXservice();
        heXservice.readLinesFromFile(verySmallFilePath);

        
        
        
        Assertions.assertEquals(heXservice.getBytes().toString(), 
             "0x50, 0x75, 0x6C, 0x76, 0x69, 0x6E, 0x61, 0x72, 0x20, 0x65, 0x6C, 0x65,"
            +"0x6D, 0x65, 0x6E, 0x74, 0x75, 0x6D, 0x20, 0x69, 0x6E, 0x74, 0x65, 0x67,"
            +"0x65, 0x72, 0x20, 0x65, 0x6E, 0x69, 0x6D, 0x20, 0x6E, 0x65, 0x71, 0x75,"
            +"0x65, 0x20, 0x76, 0x6F, 0x6C, 0x75, 0x74, 0x70, 0x61, 0x74, 0x20, 0x61,"
            +"0x63, 0x20, 0x74, 0x69, 0x6E, 0x63, 0x69, 0x64, 0x75, 0x6E, 0x74, 0x20,"
            +"0x76, 0x69, 0x74, 0x61, 0x65, 0x2E, 0x20, 0x0A, 0x46, 0x65, 0x6C, 0x69,"
            +"0x73, 0x20, 0x69, 0x6D, 0x70, 0x65, 0x72, 0x64, 0x69, 0x65, 0x74, 0x20,"
            +"0x70, 0x72, 0x6F, 0x69, 0x6E, 0x20, 0x66, 0x65, 0x72, 0x6D, 0x65, 0x6E,"
            +"0x74, 0x75, 0x6D, 0x20, 0x6C, 0x65, 0x6F, 0x20, 0x76, 0x65, 0x6C, 0x20,"
            +"0x6F, 0x72, 0x63, 0x69, 0x2E, 0x20, 0x4D, 0x61, 0x75, 0x72, 0x69, 0x73,"
            +"0x20, 0x69, 0x6E, 0x20, 0x61, 0x6C, 0x69, 0x71, 0x75, 0x61, 0x6D, 0x20,"
            +"0x73, 0x65, 0x6D, 0x20, 0x66, 0x72, 0x69, 0x6E, 0x67, 0x69, 0x6C, 0x6C,"
            +"0x61, 0x20, 0x75, 0x74, 0x20, 0x6D, 0x6F, 0x72, 0x62, 0x69, 0x2E, 0x20,"
            +"0x51, 0x75, 0x61, 0x6D, 0x20, 0x76, 0x75, 0x6C, 0x70, 0x75, 0x74, 0x61,"
            +"0x74, 0x65, 0x20, 0x64, 0x69, 0x67, 0x6E, 0x69, 0x73, 0x73, 0x69, 0x6D,"
            +"0x20, 0x73, 0x75, 0x73, 0x70, 0x65, 0x6E, 0x64, 0x69, 0x73, 0x73, 0x65,"
            +"0x20, 0x69, 0x6E, 0x20, 0x65, 0x73, 0x74, 0x2E, 0x20, 0x0A, 0x45, 0x67,"
            +"0x65, 0x74, 0x20, 0x61, 0x6C, 0x69, 0x71, 0x75, 0x65, 0x74, 0x20, 0x6E,"
            +"0x69, 0x62, 0x68, 0x20, 0x70, 0x72, 0x61, 0x65, 0x73, 0x65, 0x6E, 0x74,"
            +"0x20, 0x74, 0x72, 0x69, 0x73, 0x74, 0x69, 0x71, 0x75, 0x65, 0x20, 0x6D,"
            +"0x61, 0x67, 0x6E, 0x61, 0x20, 0x73, 0x69, 0x74, 0x20, 0x61, 0x6D, 0x65,"
            +"0x74, 0x20, 0x70, 0x75, 0x72, 0x75, 0x73, 0x20, 0x67, 0x72, 0x61, 0x76,"
            +"0x69, 0x64, 0x61, 0x2E, 0x20, 0x49, 0x70, 0x73, 0x75, 0x6D, 0x20, 0x64,"
            +"0x6F, 0x6C, 0x6F, 0x72, 0x20, 0x73, 0x69, 0x74, 0x20, 0x61, 0x6D, 0x65,"
            +"0x74, 0x20, 0x63, 0x6F, 0x6E, 0x73, 0x65, 0x63, 0x74, 0x65, 0x74, 0x75,"
            +"0x72, 0x20, 0x61, 0x64, 0x69, 0x70, 0x69, 0x73, 0x63, 0x69, 0x6E, 0x67,"
            +"0x20, 0x65, 0x6C, 0x69, 0x74, 0x20, 0x64, 0x75, 0x69, 0x73, 0x2E, 0x20,"
            +"0x46, 0x72, 0x69, 0x6E, 0x67, 0x69, 0x6C, 0x6C, 0x61, 0x20, 0x65, 0x73,"
            +"0x74, 0x20, 0x75, 0x6C, 0x6C, 0x61, 0x6D, 0x63, 0x6F, 0x72, 0x70, 0x65,"
            +"0x72, 0x20, 0x65, 0x67, 0x65, 0x74, 0x20, 0x6E, 0x75, 0x6C, 0x6C, 0x61,"
            +"0x20, 0x66, 0x61, 0x63, 0x69, 0x6C, 0x69, 0x73, 0x69, 0x2E, 0x20, 0x49,"
            +"0x6E, 0x20, 0x68, 0x65, 0x6E, 0x64, 0x72, 0x65, 0x72, 0x69, 0x74, 0x20,"
            +"0x67, 0x72, 0x61, 0x76, 0x69, 0x64, 0x61, 0x20, 0x72, 0x75, 0x74, 0x72,"
            +"0x75, 0x6D, 0x20, 0x71, 0x75, 0x69, 0x73, 0x71, 0x75, 0x65, 0x20, 0x6E,"
            +"0x6F, 0x6E, 0x20, 0x74, 0x65, 0x6C, 0x6C, 0x75, 0x73, 0x20, 0x6F, 0x72,"
            +"0x63, 0x69, 0x20, 0x61, 0x63, 0x2E, 0x20, 0x52, 0x69, 0x73, 0x75, 0x73,"
            +"0x20, 0x6E, 0x75, 0x6C, 0x6C, 0x61, 0x6D, 0x20, 0x65, 0x67, 0x65, 0x74,"
            +"0x20, 0x66, 0x65, 0x6C, 0x69, 0x73, 0x20, 0x65, 0x67, 0x65, 0x74, 0x20,"
            +"0x6E, 0x75, 0x6E, 0x63, 0x2E, 0x20, 0x55, 0x74, 0x20, 0x66, 0x61, 0x75,"
            +"0x63, 0x69, 0x62, 0x75, 0x73, 0x20, 0x70, 0x75, 0x6C, 0x76, 0x69, 0x6E,"
            +"0x61, 0x72, 0x20, 0x65, 0x6C, 0x65, 0x6D, 0x65, 0x6E, 0x74, 0x75, 0x6D,"
            +"0x20, 0x69, 0x6E, 0x74, 0x65, 0x67, 0x65, 0x72, 0x20, 0x65, 0x6E, 0x69,"
            +"0x6D, 0x20, 0x6E, 0x65, 0x71, 0x75, 0x65, 0x20, 0x76, 0x6F, 0x6C, 0x75,"
            +"0x74, 0x70, 0x61, 0x74, 0x2E, 0x20, 0x50, 0x72, 0x65, 0x74, 0x69, 0x75,"
            +"0x6D, 0x20, 0x66, 0x75, 0x73, 0x63, 0x65, 0x20, 0x69, 0x64, 0x20, 0x76,"
            +"0x65, 0x6C, 0x69, 0x74, 0x20, 0x75, 0x74, 0x2E, 0x20, 0x54, 0x72, 0x69,"
            +"0x73, 0x74, 0x69, 0x71, 0x75, 0x65, 0x20, 0x6E, 0x75, 0x6C, 0x6C, 0x61,"
            +"0x20, 0x61, 0x6C, 0x69, 0x71, 0x75, 0x65, 0x74, 0x20, 0x65, 0x6E, 0x69,"
            +"0x6D, 0x20, 0x74, 0x6F, 0x72, 0x74, 0x6F, 0x72, 0x20, 0x61, 0x74, 0x20,"
            +"0x61, 0x75, 0x63, 0x74, 0x6F, 0x72, 0x20, 0x75, 0x72, 0x6E, 0x61, 0x2E,"
            +"0x0A, 0x0A, 0x43, 0x72, 0x61, 0x73, 0x20, 0x74, 0x69, 0x6E, 0x63, 0x69,"
            +"0x64, 0x75, 0x6E, 0x74, 0x20, 0x6C, 0x6F, 0x62, 0x6F, 0x72, 0x74, 0x69,"
            +"0x73, 0x20, 0x66, 0x65, 0x75, 0x67, 0x69, 0x61, 0x74, 0x20, 0x76, 0x69,"
            +"0x76, 0x61, 0x6D, 0x75, 0x73, 0x20, 0x61, 0x74, 0x20, 0x61, 0x75, 0x67,"
            +"0x75, 0x65, 0x20, 0x65, 0x67, 0x65, 0x74, 0x2E"
        );

        System.out.println(heXservice.getBytes());

        try (Stream<Character> stream = Files.lines(path)) {
            Character[] lines = stream.toArray(Character::new);

            Assertions.assertEquals(heXservice.getStrings(), lines);
        } catch (IOException exception) { System.out.println(exception.getMessage()); }


    }

    @Test
    void FileViewerTest() {
        FileViewer fileViewer = new FileViewer(verySmallFilePath);

    }
}
