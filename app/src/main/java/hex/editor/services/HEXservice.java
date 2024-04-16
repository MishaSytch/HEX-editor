package hex.editor.services;

import java.util.*;

public class HEXservice {
    private FileViewer fileViewer;
    private List<String> lines;    

    public void readLinesFromFile(String path) {
        fileViewer = new FileViewer(path);
        lines = fileViewer.getLines();
    }

    public Byte[] getBytes() {
        return lines.stream().map(x -> x.getBytes()).toArray(size -> new Byte[size]);
    }

    public Character[] getChars() {
        return lines.stream().map(x -> x.toCharArray()).toArray(size -> new Character[size]);
    }
    
}
