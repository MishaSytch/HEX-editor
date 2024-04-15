package hex.editor.services;

import java.util.*;

public class HEXservice {
    FileViewer fileViewer;
    List<String> lines;
    int from = 0;
    int rowCounts;    

    public void readLinesFromFile(String path) {
        fileViewer = new FileViewer(path);
        lines = fileViewer.getLines();
    }

    public Byte[] getBytes() {
        return (Byte[]) lines.stream()
            .map(x -> {return x.getBytes();}).toArray();
    }
    
}
