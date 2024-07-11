package hex.editor.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HexServiceTest {
    String line;
    List<String> hex;

    @BeforeEach
    void setUp() {
        line = "Hello World!";
        hex = new ArrayList<>();
        hex.addAll(Arrays.asList("48 65 6C 6C 6f 20 57 6f 72 6C 64 21".toLowerCase().split(" ")));
    }

    @Test
    void getHexFromString() {
        Assertions.assertEquals(hex, HexService.getHexFromString(line));
    }

    @Test
    void getCharsFromHex() {
        Assertions.assertEquals(line, HexService.getCharsFromHex(hex).stream().collect(Collectors.joining()));
    }
}