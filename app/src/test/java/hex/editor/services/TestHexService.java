package hex.editor.services;

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
    void testGetHex_one() {
        String[] hex = HexService.getHexFromString("а");
        String[] res = new String[]{Integer.toHexString((byte)'а')};

        Assertions.assertArrayEquals(hex, res);
    }

    @Test 
    void testGetHex_none() {
        String[] hex = HexService.getHexFromString("\n");
        String[] res = new String[]{Integer.toHexString((byte)'\n')};

        Assertions.assertArrayEquals(hex, res);
    }

    @Test
    void testGetHexFromString_small() {
        String[] res = {
            "50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65", "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67", "65", "72", "2E", 
            "0A", "0A",
            "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20", "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E", "20", "0447", "0438", "0441", "043B", "0430", "21"
        };

        String[] hex = HexService.getHexFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");

        Assertions.assertArrayEquals(hex, res);
    }

    @Test
    void testGetCharsFromString_small() {
        String[] lines = "Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!".split("");
        Assertions.assertArrayEquals(HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!"), lines);
    }

    @Test
    void testGetCharsFromHex_small() {
        String[] hex = HexService.getHexFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");
        String[] chars = HexService.getCharsFromHex(hex);

        String[] res = HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");

        Assertions.assertArrayEquals(chars, res);
    }

    @Test
    void testGetHexFromChars_small() {
        String[] chars = HexService.getCharsFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");
        String[] hex = HexService.getHexFromChars(chars);

        String[] res = HexService.getHexFromString("Pulvinar elementum integer.\n\nЗаморозки наступили 3-го числа!");

        Assertions.assertArrayEquals(hex, res);
    }
}
