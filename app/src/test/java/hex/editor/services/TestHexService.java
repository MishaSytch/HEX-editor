package hex.editor.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestHexService {

    @Test
    void getHexFromString_eng_symbol() {
        String symbol = "E";
        List<String> hex = HexService.getHexFromString(symbol);
        assertEquals(1, hex.size());
        assert hex.get(0).equals("45");
    }

    @Test
    void getHexFromString_eng_symbols() {
        String symbols = "Eng 12!";
        List<String> hex = HexService.getHexFromString(symbols);
        assertEquals(7, hex.size());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < symbols.length(); i++) {
            res.add(Integer.toHexString(symbols.charAt(i)));
        }
        assertEquals(res, hex);
    }

    @Test
    void testGetHexFromString_rus_symbol() {
        String symbol = "П";
        List<String> hex = HexService.getHexFromString(symbol);
        assertEquals(2, hex.size());
        assertEquals("d0", hex.get(0));
        assertEquals("9f", hex.get(1));
    }

    @Test
    void getHexFromString_rus_symbols() {
        String symbols = "Привет";
        List<String> hex = HexService.getHexFromString(symbols);
        assertEquals(12, hex.size());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < symbols.length(); i++) {
            res.add(Integer.toHexString(symbols.charAt(i) & 0xFF));
        }
        assertEquals(res, hex);
    }

    @Test
    void getCharsFromString_eng_symbol() {
        String symbol = "E";
        List<String> hex = HexService.getCharsFromString(symbol);
        assertEquals(1, hex.size());
        assertEquals(symbol, hex.get(0));
    }

    @Test
    void getCharsFromString_eng_symbols() {
        String symbols = "Eng 12!";
        List<String> hex = HexService.getCharsFromString(symbols);
        assertEquals(7, hex.size());
        int i = 0;
        for (String st : symbols.split("")) {
            assertEquals(st, hex.get(i++));
        }
    }

    @Test
    void getCharsFromHex_eng_symbol() {
        String symbol = "E";
        List<String> hex = HexService.getCharsFromHex(HexService.getHexFromString(symbol));
        assertEquals(1, hex.size());
        assertEquals(symbol, hex.get(0));
    }

    @Test
    void getCharsFromHex_eng_symbols() {
        String symbols = "Eng 12!";
        List<String> hex = HexService.getCharsFromHex(HexService.getHexFromString(symbols));
        assertEquals(7, hex.size());
        int i = 0;
        for (String st : symbols.split("")) {
            assertEquals(st, hex.get(i++));
        }
    }

    @Test
    void getHexFromChars_eng_symbol() {
        String symbol = "E";
        List<String> hex = HexService.getHexFromChars(HexService.getCharsFromString(symbol));
        assertEquals(1, hex.size());
        assertEquals(Integer.toHexString(symbol.charAt(0) & 0xFF), hex.get(0));
    }

    @Test
    void getHexFromChars_eng_symbols() {
        String symbols = "Eng 12!";
        List<String> hex = HexService.getHexFromChars(HexService.getCharsFromString(symbols));
        assertEquals(7, hex.size());
        int i = 0;
        for (String st : symbols.split("")) {
            assertEquals(Integer.toHexString(st.charAt(0) & 0xFF), hex.get(i++));
        }
    }
}
