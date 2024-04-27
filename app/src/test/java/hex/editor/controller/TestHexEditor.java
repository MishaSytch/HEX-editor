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
       });
        
    }

    @Test
    void testGetCharsString() {
        hexEditor = new HexEditor(verySmallFilePath);

        Assertions.assertArrayEquals(hexEditor.getCharsString(), "Pulvinar elementum integer.".split(""));
    }

    @Test
    void getCharFromHex_normal() {
        hexEditor = new HexEditor(verySmallFilePath);
        String ch = hexEditor.getCharFromHex("044B");

        Assertions.assertEquals(ch, "ы");
    }

    @Test
    void getCharFromHex_NotValid() {

        Assertions.assertThrows(NumberFormatException.class, () -> {
            hexEditor = new HexEditor(verySmallFilePath);
            hexEditor.getCharFromHex("044S");
        });
    }

    @Test
    void getHexFromChar() {
        hexEditor = new HexEditor(verySmallFilePath);
        Assertions.assertEquals(new String("044B"), hexEditor.getHexFromChars(new String[]{"ы"})[0]);
    }

    @Test
    void getCharsFromHex(String[] hex) {
        return HexService.getCharsFromHex(hex);
    }

    @Test
    void getCharsFromHex_NotValid() {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            hexEditor = new HexEditor(verySmallFilePath);
            hexEditor.getCharsFromHex(new String[]{ "044S", "null"});
        });
    }

    @Test
    void getHexFromChars(String[] chars) {
        return HexService.getHexFromChars(chars);
    }

    @Test
    void testEditOpenedFileByHex() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] hex = hexEditor.getHexString();
        hex[0] = hex[hex.length - 1];
        hexEditor.editOpenedFileByHex(hex);

        Assertions.assertArrayEquals(hexEditor.getHexString(), new String[]{
            "2E", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
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

    @Test 
    void testFindOne() {
        hexEditor = new HexEditor(verySmallFilePath);
        Integer[] hex = hexEditor.find(new String[]{"20"});

        Assertions.assertArrayEquals(hex, new Integer[] { 8, 18});
    }

    @Test 
    void testFindTwo() {
        hexEditor = new HexEditor(verySmallFilePath);
        Integer[] hex = hexEditor.find(new String[]{"2"});

        Assertions.assertArrayEquals(hex, new Integer[] {});
    }

    @Test 
    void findByMask_Simple() {
        hexEditor = new HexEditor(verySmallFilePath);
        Integer[] hex = hexEditor.findByMask("20");
        
        Assertions.assertArrayEquals(hex, new Integer[]{ 8, 18 });
    }

    @Test 
    void findByMask_One() {
        hexEditor = new HexEditor(verySmallFilePath);
        Integer[] hex = hexEditor.findByMask("2+");
        
        Assertions.assertArrayEquals(hex, new Integer[]{ 7, 8, 18, 25, 26 });
    }
}
