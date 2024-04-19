package hex.editor.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTableViewer {
    @Test
    void testGetTable_Chars() {
        TableViewer tableViewer = new TableViewer();

        String[][] tb = tableViewer.getTable("Pulvinar ".split(""), 3);
        String[][] res = new String[][] {
            new String[]{"P", "u", "l"},
            new String[]{"v", "i", "n"},
            new String[]{"a", "r", " "}
        };
        
        Assertions.assertArrayEquals(tb, res);
    }

    @Test
    void testGetTable_Hex() {
        TableViewer tableViewer = new TableViewer();

        String[][] tb = tableViewer.getTable(HexService.getHexFromString("Pulvinar "), 3);
        String[][] res = new String[][] {
            new String[]{"50", "75", "6C"},
            new String[]{"76", "69", "6E"},
            new String[]{"61", "72", "20"}
        };
        
        Assertions.assertArrayEquals(tb, res);
    }
}
