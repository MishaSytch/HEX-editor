package hex.editor.services;

import java.util.*;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

public class TableViewer {
    public static TableModel getTable(String[] data, int columns_count) {
        DefaultTableModel tableModel = new DefaultTableModel();
        Vector<Integer> columns = new Vector<Integer>();
        for (int i = 0; i < columns_count; i++) columns.add(i);

        tableModel.setColumnIdentifiers(columns);

        int rows_count = (int)Math.ceil(data.length / columns_count);
        int data_i = 0;

        for(int i = 0; i < rows_count; i++) {
            Vector<String> inner = new Vector<String>();
            inner.add(String.valueOf(i));
            for (int k = 1; k < columns_count; k++) {
                inner.add(data[data_i++]);
            }
            tableModel.addRow(inner);
        }
        
        return tableModel;
    }
}
