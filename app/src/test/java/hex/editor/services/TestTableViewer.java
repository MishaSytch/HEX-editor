package hex.editor.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTableViewer {
    @Test
    void testGetTable_Chars() {
        String[][] tb = TableViewer.getTable("Pulvinar ".split(""), 3);
        String[][] res = new String[][] {
            new String[]{"0", "P", "u", "l"},
            new String[]{"1", "v", "i", "n"},
            new String[]{"2", "a", "r", " "}
        };
        
        Assertions.assertArrayEquals(tb, res);
    }

    @Test
    void testGetTable_Hex() {
        String[][] tb = TableViewer.getTable(HexService.getHexFromString("Pulvinar "), 3);
        String[][] res = new String[][] {
            new String[]{"0", "50", "75", "6C"},
            new String[]{"1", "76", "69", "6E"},
            new String[]{"2", "61", "72", "20"}
        };
        
        Assertions.assertArrayEquals(tb, res);
    }
}
