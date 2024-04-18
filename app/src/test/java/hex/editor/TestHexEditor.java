package hex.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hex.editor.controller.HexEditor;

public class TestHexEditor {
    @Test
    void testHexEditor() {
        HexEditor hex = new HexEditor();
        Assertions.assertNotNull(hex);
    }
}
