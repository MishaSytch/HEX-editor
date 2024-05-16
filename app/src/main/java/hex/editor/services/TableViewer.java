package hex.editor.services;

import java.util.*;

import javax.swing.table.DefaultTableModel;

import hex.editor.controller.HexEditor;

public class TableViewer {
    private static final HexEditor hexEditor = new HexEditor();

    public static DefaultTableModel getTable(List<List<String>> lines, int columns, boolean isHex) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        Vector<String> columnNames = new Vector<>();
        columnNames.add(String.valueOf(' '));
        for (int i = 0; i < columns; i++) columnNames.add(String.valueOf(i));

        tableModel.setColumnIdentifiers(columnNames);

        int rows_count = lines.size();
        for(int i = 0; i < rows_count; i++) {
            List<String> line = lines.get(i);
            Vector<String> inner = new Vector<>();
            inner.add(String.valueOf(i));
            
            for (int k = 0; k < columns; k++) {
                if (k >= line.size()) {
                    inner.add("");
                } else {
                    if (isHex)inner.add(hexEditor.getHexFromChar(line.get(k)));
                    else inner.add(line.get(k));
                }
            }
            tableModel.addRow(inner);
        }


        return tableModel;
    }
}
