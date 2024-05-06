package hex.editor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;
import hex.editor.services.HexService;

public class TestHexEditor {
    static String bigFilePath; 
    static String verySmallFilePath;
    static String oneCharPath;
    static HexService heXservice;
    static HexEditor hexEditor;

    @BeforeAll
    static void setup() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
        oneCharPath = FilePaths.getOneChar();
        // HexService instantiation is not needed since its methods are static
    }

    @Test
    void testGetHexStringCorrectly() {
        hexEditor = new HexEditor(verySmallFilePath);
        List<List<String>> hex = hexEditor.getHexLines();
        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E"));

        Assertions.assertEquals(expected, hex);
    }

    @Test
    void testOpenNewFile() {
        hexEditor = new HexEditor(oneCharPath);
        hexEditor.openNewFile(verySmallFilePath);
        List<String> hex = hexEditor.getStrings();
        List<String> res = new ArrayList<>();
        res.add("Pulvinar elementum integer.");
        res.add("");

        Assertions.assertEquals(res, hex);
    }

    @Test
    void testGetHexString() {
        hexEditor = new HexEditor(verySmallFilePath);
        List<List<String>> hex = hexEditor.getHexLines();
        List<List<String>> res = new ArrayList<>();

        for (String st : new String[]{"50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E"}) {
            
        }
        
        

        Assertions.assertEquals(hex, res);
        
    }

    @Test
    void testGetCharsString() {
        hexEditor = new HexEditor(verySmallFilePath);

        Assertions.assertArrayEquals(hexEditor.getCharLines(), "Pulvinar elementum integer.".split(""));
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
        Assertions.assertArrayEquals(new String[]{}, hexEditor.getCharsFromHex(new String[]{}));
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

    }

    @Test
    void testEditOpenedFileByHex() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] hex = hexEditor.getHexLines();
        hex[0] = hex[hex.length - 1];
        hexEditor.editOpenedFileByHex(hex);

        Assertions.assertArrayEquals(hexEditor.getHexLines(), new String[]{
            "2E", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
        });
    } 

    @Test
    void testEditOpenedFileByChars() {
        hexEditor = new HexEditor(verySmallFilePath);
        String[] chars = hexEditor.getCharLines();
        chars[0] = chars[chars.length - 1];
        hexEditor.editOpenedFileByChars(chars);

        Assertions.assertArrayEquals(hexEditor.getCharLines(), ".ulvinar elementum integer.".split(""));
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
