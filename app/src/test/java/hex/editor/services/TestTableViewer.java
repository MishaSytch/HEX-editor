package hex.editor.services;

import org.junit.gen5.api.Assertions;
import org.junit.gen5.api.Test;

public class TestTableViewer {
    @Test
    void getTable() {
        TableViewer tableViewer = new TableViewer();

        String[][] tb = tableViewer.getTable("Pulvinar ".split(""), 3);
        String[][] res = new String[][] {
            new String[]{"P", "u", "l"},
            new String[]{"v", "i", "n"},
            new String[]{"a", "r", " "}
        };
        
        Assertions.assertEquals(tb, res);
    }
}
