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
        Assertions.assertArrayEquals(hexEditor.getCharsString(), new String[]{"в"});
    }

    @Test
    void testOpenNewFile(String path) {
        hexEditor = new HexEditor(oneChar);
        hexEditor.openNewFile(verySmallFilePath);
        
        Assertions.assertArrayEquals(hexEditor.getCharsString(), "Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!".split(""));
    }

    @Test
    void testGetHexString() {
        
    }

    @Test
    void testGetCharsString() {
        
    }

    @Test
    void testEditOpenedFileByHex(String[] hex) {
        
    } 

    @Test
    void testEditOpenedFileByChars(String[] chars) {
        
    } 
}
