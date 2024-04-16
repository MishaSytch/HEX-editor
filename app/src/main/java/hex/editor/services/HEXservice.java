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
        return lines.stream().map(x -> {return x.getBytes();}).toArray(size -> new Byte[size]);
    }

    public Character[] getCharss() {
        return lines.stream().toArray(size -> new Character[size]);
    }
    
}
