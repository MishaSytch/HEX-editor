package hex.editor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class InfoTest {

    @Test
    public void testGetChar() {
        Info info = new Info(1, 1, "A", "41");
        assertEquals("A", info.getChar());
    }

    @Test
    public void testGetHex() {
        Info info = new Info(1, 1, "A", "41");
        assertEquals("41", info.getHex());
    }

    @Test
    public void testGetInfo() {
        Info info = new Info(1, 1, "A", "41");
        String expected = "<html>Row: 1<br>Column: 1<br>Char: A<br>Hex: 41<br><br>8-bit value: 65<br>Unsigned value: 65<br>";
        assertEquals(expected, info.getInfo());
    }

    @Test
    public void testGetBytes() {
        List<Info> infos = new ArrayList<>();
        infos.add(new Info(1, 1, "A", "41"));
        infos.add(new Info(2, 2, "B", "42"));
        String expected = "<html>16-bit signed: <br>16961<br><br>16-bit unsigned: <br>16961<br><br>";
        assertEquals(expected, Info.getBytes(infos));
    }
}