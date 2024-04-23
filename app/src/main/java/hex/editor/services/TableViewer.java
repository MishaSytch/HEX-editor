package hex.editor.services;

import java.awt.Toolkit;
import java.util.*;

import javax.swing.table.DefaultTableModel;

public class TableViewer {

    private static int size = Toolkit.getDefaultToolkit().getScreenResolution() / 2;

    public static DefaultTableModel getTable(String[] data, int width) {
        int columns_count = (int) Math.round(width / size);
        DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        Vector<String> columnNames = new Vector<String>();
        for (int i = 0; i < columns_count; i++) columnNames.add(String.valueOf(i));

        tableModel.setColumnIdentifiers(columnNames);

        int rows_count;
        if (data.length % columns_count != 0) 
            rows_count = (int)Math.round(data.length / columns_count) + 1;
         else 
            rows_count = (int)Math.round(data.length / columns_count);

        int data_i = 0;
        for(int i = 0; i < rows_count; i++) {
            Vector<String> inner = new Vector<String>();
            for (int k = 0; k < columns_count; k++) {
                if (data_i == data.length) {
                    inner.add("");
                } else {
                    inner.add(data[data_i++]);
                }
            }
            tableModel.addRow(inner);
        }

        return tableModel;
    }
}
