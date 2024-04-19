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
        Assertions.assertEquals(hexEditor.getStrings(), "B");
    
        hexEditor.openNewFile(verySmallFilePath);
        
        Assertions.assertEquals(hexEditor.getStrings(), "Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");
    }

    @Test
    void testGetHexString() {
        hexEditor = new HexEditor(verySmallFilePath);

        Assertions.assertArrayEquals(hexEditor.getHexString(), new String[]{
            "50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
            "0A", "0A",
            "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20", "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E", "20", "0447", "0438", "0441", "043B", "0430", "21"
        });
        
    }

    @Test
    void testGetCharsString() {
        hexEditor = new HexEditor(verySmallFilePath);

        Assertions.assertArrayEquals(hexEditor.getCharsString(), "Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!".split(""));
    }

    @Test
    void testEditOpenedFileByHex() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] hex = hexEditor.getHexString();
        hex[0] = hex[hex.length - 1];

        hexEditor.editOpenedFileByHex(hex);

        hex = hexEditor.getHexString();

        String str = hexEditor.getStrings();

        Assertions.assertArrayEquals(hex, new String[]{
            "21", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
            // "0A", "0A",
            "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20", "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E", "20", "0447", "0438", "0441", "043B", "0430", "21"
        });
    } 

    @Test
    void testEditOpenedFileByChars(String[] chars) {
        
    } 
}
