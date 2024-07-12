package hex.editor.controller;

import hex.editor.model.Position;
import hex.editor.model.Positions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class HexEditorTest {
    String _hex;
    String _mask;
    List<List<String>> _matrixHex;
    List<String> _listHex;
    List<String> searchingHex;
    Positions positions;

    @BeforeEach
    void setUp() {
        _hex = "A2";

        _mask = "6";

        _matrixHex = new ArrayList<>();
        ArrayList<String> inner = new ArrayList<>();
        for (String hex : "48 65 6C 6f 20 57 6f 72 6C 64 21".split(" ")) {
            inner.add(hex);
            if (inner.size() == 2) {
                _matrixHex.add(inner);
                inner = new ArrayList<>();
            }
        }
        _matrixHex.add(inner);

        _listHex = new ArrayList<>();
        _listHex.addAll(Arrays.asList("48 65 6C 6f 20 57 6f 72 6C 64 21".split(" ")));

        searchingHex = new ArrayList<>();
        searchingHex.addAll(Arrays.asList("48 65 6C".split(" ")));

        positions = new Positions();
    }

    @Test
    void getCharFromHex() {
        Assertions.assertThrows(NullPointerException.class, () -> HexEditor.getCharFromHex(null));
        Assertions.assertThrows(NumberFormatException.class, () -> HexEditor.getCharFromHex("GG"));
        Assertions.assertThrows(NumberFormatException.class, () -> HexEditor.getCharFromHex(" "));

        Assertions.assertDoesNotThrow(() -> HexEditor.getCharFromHex("FF"));
        Assertions.assertDoesNotThrow(() -> HexEditor.getCharFromHex(""));

        Assertions.assertEquals("H", HexEditor.getCharFromHex("48"));
        Assertions.assertEquals(" ", HexEditor.getCharFromHex("20"));
    }

    @Test
    void find() {
        positions.removeAll();
        HexEditor.find(positions, searchingHex, _matrixHex);

        Assertions.assertEquals(new Position(0, 0).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(0, 0).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(0, 1).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(0, 1).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(1, 0).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(1, 0).getRow(), positions.getCurrent().getRow());
    }

    @Test
    void findByMask() {
        HexEditor.findByMask(positions, _mask, _matrixHex);

        Assertions.assertEquals(new Position(0, 1).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(0, 1).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(1, 0).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(1, 0).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(1, 1).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(1, 1).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(3, 0).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(3, 0).getRow(), positions.getCurrent().getRow());
        positions.getNext();
        Assertions.assertEquals(new Position(4, 0).getColumn(), positions.getCurrent().getColumn());
        Assertions.assertEquals(new Position(4, 0).getRow(), positions.getCurrent().getRow());

    }
}