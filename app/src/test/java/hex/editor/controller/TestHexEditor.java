package hex.editor.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestHexEditor {
    @Test
    void testHexEditor() {
        HexEditor hex = new HexEditor();
        Assertions.assertNotNull(hex);
    }
}
