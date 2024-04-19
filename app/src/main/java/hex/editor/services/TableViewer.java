package hex.editor.services;

import java.util.*;

public class TableViewer {
    public static String[][] getTable(String[] data, int colomns) {
        List<String[]> list = new ArrayList<String[]>();
        
        int i = 0;
        String[] tmp = new String[colomns];
        for (String str : data) {
            if (i == colomns) {
                list.add(tmp);
                tmp = new String[colomns];
                i = 0;
            }
            tmp[i++] = str;
        }
        list.add(tmp);
        return list.toArray(String[][]::new);
    }
}
