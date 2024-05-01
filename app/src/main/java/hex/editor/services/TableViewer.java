package hex.editor.services;

import java.util.*;

import javax.swing.table.DefaultTableModel;

public class TableViewer {

    public static DefaultTableModel getTable(List<List<String>> lines) {
        int columns_count = 0;
        for (List<String> line : lines) {
            columns_count = Math.max(columns_count, line.size());
        }
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Запретить редактирование для колонки 0
                return column != 0;
            }
        };

        Vector<String> columnNames = new Vector<String>();
        columnNames.add(String.valueOf(""));
        for (int i = 0; i < columns_count - 1 ; i++) columnNames.add(String.valueOf(i));

        tableModel.setColumnIdentifiers(columnNames);

        int rows_count = lines.size();

        for(int i = 0; i < rows_count; i++) {
            List<String> line = lines.get(i);
            Vector<String> inner = new Vector<String>();
            inner.add(String.valueOf(i));
            for (int k = 1; k < columns_count; k++) {
                if (k >= line.size()) {
                    inner.add("");
                } else {
                    inner.add(line.get(k));
                }
            }
            tableModel.addRow(inner);
        }

        return tableModel;
    }
}
