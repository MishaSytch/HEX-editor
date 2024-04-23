package hex.editor.services;

import java.awt.Toolkit;
import java.util.*;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

public class TableViewer {

    private static int size = Toolkit.getDefaultToolkit().getScreenResolution() / 2;

    public static TableModel getTable(String[] data, int width) {
        int columns_count = (int) Math.round(width / size);
        DefaultTableModel tableModel = new DefaultTableModel();
        Vector<Integer> columns = new Vector<Integer>();
        for (int i = 0; i < columns_count; i++) columns.add(i);

        tableModel.setColumnIdentifiers(columns);

        int rows_count;
        if (data.length % columns_count != 0) 
            rows_count = (int)Math.round(data.length / columns_count) + 1;
         else 
            rows_count = (int)Math.round(data.length / columns_count);

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
