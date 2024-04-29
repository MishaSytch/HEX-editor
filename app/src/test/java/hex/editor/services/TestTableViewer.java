package hex.editor.services;

import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestTableViewer {
    @Test
    void testGetTable_Chars() {
        DefaultTableModel tb = TableViewer.getTable(new String[]{"Pulvinar "});
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = new String[]{"0", "1", "2", "3"};
        res.setColumnIdentifiers(columns);

        res.addRow(new String[]{"0", "P", "u", "l"});
        res.addRow(new String[]{"1", "v", "i", "n"});
        res.addRow(new String[]{"2", "a", "r", " "});

        Assertions.assertEquals(res.getColumnCount(), tb.getColumnCount());
        Assertions.assertEquals(res.getRowCount(), tb.getRowCount());

        for (int k = 0; k < tb.getRowCount(); k++) {
            for (int i = 0; i < tb.getColumnCount(); i++) {
                Assertions.assertEquals(res.getValueAt(k, i), tb.getValueAt(k, i));
            }
            Assertions.assertEquals(tb.getColumnName(k), res.getColumnName(k));
        }
    }

    @Test
    void testGetTable_Hex() {
        DefaultTableModel tb = TableViewer.getTable(HexService.getHexFromString("Pulvinar "));
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = new String[]{"0", "1", "2", "3"};
        res.setColumnIdentifiers(columns);

        res.addRow(new String[]{"0", "50", "75", "6C"});
        res.addRow(new String[]{"1", "76", "69", "6E"});
        res.addRow(new String[]{"2", "61", "72", "20"});

        Assertions.assertEquals(res.getColumnCount(), tb.getColumnCount());
        Assertions.assertEquals(res.getRowCount(), tb.getRowCount());

        for (int k = 0; k < tb.getRowCount(); k++) {
            for (int i = 0; i < tb.getColumnCount(); i++) {
                Assertions.assertEquals(res.getValueAt(k, i), tb.getValueAt(k, i));
            }
            Assertions.assertEquals(tb.getColumnName(k), res.getColumnName(k));
        }
    }

    @Test
    void testGetTable_OneChear() {
        DefaultTableModel tb = TableViewer.getTable(HexService.getHexFromString("F"));
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = new String[]{"0", "1", "2", "3"};
        res.setColumnIdentifiers(columns);

        res.addRow(new String[]{"0", "46", "", ""});

        Assertions.assertEquals(res.getColumnCount(), tb.getColumnCount());
        Assertions.assertEquals(res.getRowCount(), tb.getRowCount());

        for (int k = 0; k < tb.getRowCount(); k++) {
            for (int i = 0; i < tb.getColumnCount(); i++) {
                Assertions.assertEquals(res.getValueAt(k, i), tb.getValueAt(k, i));
            }
            Assertions.assertEquals(tb.getColumnName(k), res.getColumnName(k));
        }
    }
}
