package hex.editor.services;

import java.util.stream.Collectors;
import java.util.Arrays;

public class HEXservice {
    private FileViewer fileViewer;
    private String lines;  

    public void readLinesFromFile(String path) {
        fileViewer = new FileViewer(path);
        lines = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public String[] getHex() {
        return Arrays.stream(lines.split(""))
            .map(s -> s.charAt(0))
            .map(c -> String.format("%04x", (int)c))
            .toArray(String[]::new);
    }

    public Byte[] getBytes() {
        return Arrays.stream(getHex())
            .map(x -> Byte.valueOf(x, 16))
            .toArray(Byte[]::new);
    }

    public String[] getChars() {
        return lines.split("");
    }
}
