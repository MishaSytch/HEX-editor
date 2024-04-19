package hex.editor.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;
import hex.editor.services.HexService;

public class TestHexEditor {
    static String bigFilePath; 
    static String verySmallFilePath;
    static String oneChar;
    static HexService heXservice;
    static HexEditor hexEditor;

    @BeforeAll
    static void getter() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
        oneChar = FilePaths.getOneChar();
        heXservice = new HexService();
    }
    
    @Test
    void testHexEditor_oneChar() {
        hexEditor = new HexEditor(oneChar);
        String[] hex = hexEditor.getCharsString();
        Assertions.assertArrayEquals(hex, new String[]{"B"});
    }

    @Test
    void testOpenNewFile() {
        hexEditor = new HexEditor(oneChar);
        hexEditor.openNewFile(verySmallFilePath);
        String res = hexEditor.getString();
        Assertions.assertEquals(res, "Pulvinar elementum integer.");
    }

    @Test
    void testGetHexString() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] res = hexEditor.getHexString();

        Assertions.assertArrayEquals(res, new String[]{
            "50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
            // "0A", "0A",
            // "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20", "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E", "20", "0447", "0438", "0441", "043B", "0430", "21"
        });
        
    }

    @Test
    void testGetCharsString() {
        hexEditor = new HexEditor(verySmallFilePath);

        Assertions.assertArrayEquals(hexEditor.getCharsString(), "Pulvinar elementum integer.".split(""));
    }

    @Test
    void testEditOpenedFileByHex() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] hex = hexEditor.getHexString();
        hex[0] = hex[hex.length - 1];
        hexEditor.editOpenedFileByHex(hex);

        Assertions.assertArrayEquals(hexEditor.getHexString(), new String[]{
            "2E", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
            // "0A", "0A",
            // "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20", "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E", "20", "0447", "0438", "0441", "043B", "0430", "21"
        });
    } 

    @Test
    void testEditOpenedFileByChars() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] chars = hexEditor.getCharsString();
        chars[0] = chars[chars.length - 1];
        hexEditor.editOpenedFileByChars(chars);

        Assertions.assertArrayEquals(hexEditor.getCharsString(), ".ulvinar elementum integer.".split(""));
    } 
}
