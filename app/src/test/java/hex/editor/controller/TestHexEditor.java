package hex.editor.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;
import hex.editor.controller.HexEditor;
import hex.editor.services.HEXservice;

public class TestHexEditor {
    static String bigFilePath; 
    static String verySmallFilePath;
    static String oneChar;
    static HEXservice heXservice;
    static HexEditor hexEditor;

    @BeforeAll
    static void getter() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
        oneChar = FilePaths.getOneChar();
        heXservice = new HEXservice();
    }
    
    @Test
    void testHexEditor_oneChar() {
        hexEditor = new HexEditor(oneChar);
        Assertions.assertArrayEquals(hexEditor.getCharsString(), new String[]{"в"});
    }

    void testOpenNewFile(String path) {
        hexEditor = new HexEditor(oneChar);

    }

    void testGetHexString() {
        hexService.getHexFromString(strings);
    }

    void testGetCharsString() {
        hexService.getCharsFromString(strings);
    }

    void testEditOpenedFileByHex(String[] hex) {
        String.join(strings, hexService.getCharsFromHex(hex));
    } 

    void testEditOpenedFileByChars(String[] chars) {
        String.join(strings, chars);
    } 
}
