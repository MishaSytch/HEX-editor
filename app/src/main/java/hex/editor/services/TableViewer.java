package hex.editor.services;

import java.util.*;

public class TableViewer {
    public static String[][] getTable(String[] data, int colomns_count) {
        List<String[]> list = new ArrayList<String[]>();
        
        int row = 0;
        int rows_count = (int)Math.ceil(data.length / colomns_count);
        String[][] outer = new String[rows_count][colomns_count + 1];
        int data_i = 0;

        for(String[] inner : outer) {
            inner[0] = String.valueOf(row++);
            for (int i = 1; i < inner.length; i++) {
                inner[i] = data[data_i++];
            }
        }
        return outer;
    }
}
