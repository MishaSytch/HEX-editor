package hex.editor.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import hex.editor.model.Position;
import hex.editor.model.Positions;

public class TestHexEditor {

    @Test
    void testFind_Shift() {
        String findinfString = "20 ef 30";
        String poligon = "20 ef 30 ad da df ed fe 20 ef 20 ef";
        List<String> searchingHex = new ArrayList<>();
        for (String st : findinfString.split(" ")) {
            searchingHex.add(st);
        }
        List<List<String>> outer = new ArrayList<>();
        List<String> inner = new ArrayList<>();
        for (String st : poligon.split(" ")) {
            if (inner.size() == 3) {
                outer.add(inner);
                inner = new ArrayList<>();
            }
            inner.add(st);
        }
        outer.add(inner);

        Positions result = new Positions();
        result.add(new Position(0, 0));
        result.add(new Position(0, 1));
        result.add(new Position(0, 2));

        Positions hex = new Positions();
        HexEditor.find(hex, searchingHex, outer);
    }

}