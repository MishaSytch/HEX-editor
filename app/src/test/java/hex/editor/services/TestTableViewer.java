package hex.editor.services;

import org.junit.gen5.api.Assertions;
import org.junit.gen5.api.Test;

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
        
        Assertions.assertEquals(tb, res);
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
        
        Assertions.assertEquals(tb, res);
    }
}
