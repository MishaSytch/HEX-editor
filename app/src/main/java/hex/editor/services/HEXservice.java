package hex.editor.services;

import java.util.*;
import java.util.stream.Collectors;

public class HEXservice {
    private FileViewer fileViewer;
    private String lines;    

    public void readLinesFromFile(String path) {
        fileViewer = new FileViewer(path);
        lines = fileViewer.getLines().stream().collect(Collectors.joining(" "));
    }

    public byte[] getBytes() {
        return lines.getBytes();
    }

    public char[] getChars() {
        return lines.toCharArray();
    }
    
}
