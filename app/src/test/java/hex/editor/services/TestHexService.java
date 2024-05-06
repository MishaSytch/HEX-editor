package hex.editor.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;


public class TestHexService {
    static String bigFilePath; 
    static String verySmallFilePath;
    static String oneChar;

    @BeforeAll
    static void getter() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
        oneChar = FilePaths.getOneChar();
    }

    @Test
    public void testGetHexFromStringReturnsHexList() {

        String line = "Hello World";
    
        List<String> result = HexService.getHexFromString(line);

        List<String> expected = Arrays.asList("48", "65", "6C", "6C", "6F", "20", "57", "6F", "72", "6C", "64");
        Assertions.assertEquals(expected, result);
    }

    // getHexFromString method handles null input string
    @Test
    public void testGetHexFromStringHandlesNullInput() {
        String line = null;

        Assertions.assertThrows(NullPointerException.class, () -> {
            HexService.getHexFromString(line);
        });
    }

    @Test 
    void testGetHex_one() {
        List<String> hex = HexService.getHexFromString("a");
        List<String> res = new ArrayList<>();
        res.add(Integer.toHexString((byte)'a'));

        Assertions.assertEquals(hex, res);
    }

    @Test 
    void testGetHex_none() {
        List<String> hex = HexService.getHexFromString("B");
        List<String> res = new ArrayList<>();
        res.add(Integer.toHexString((byte)'B'));

        Assertions.assertEquals(res, hex);
    }

    @Test
    void testGetHexFromString_small() {
        List<String> res = new ArrayList<>();
        for (String st : new String[]{"50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", "0A"}) {
            res.add(st);
        }

        List<String> hex = HexService.getHexFromString("Pulvinar elementum integer.\n");

        Assertions.assertEquals(res, hex);
    }

    @Test
    void testGetCharsFromString_small() {
        List<String> res = new ArrayList<>();
        for (String str : new String("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!").split(""))
            res.add(str);
        Assertions.assertEquals(res, HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!"));
    }

    @Test
    void testGetCharsFromHex_small() {
        List<String> hex = HexService.getHexFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");
        List<String> chars = HexService.getCharsFromHex(hex);
        List<String> res = HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");

        Assertions.assertEquals(res, chars);
    }

    @Test
    void testGetHexFromChars_small() {
        List<String> chars = HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");
        List<String> hex = HexService.getHexFromChars(chars);
        List<String> res = HexService.getHexFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");

        Assertions.assertEquals(res, hex);
    }
}