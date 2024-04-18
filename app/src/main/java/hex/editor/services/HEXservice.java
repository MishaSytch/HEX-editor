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
            .map(c -> {
                if((int)c > 256) return String.format("%04x", (int)c).toUpperCase();
                else return String.format("%02x", (int)c).toUpperCase();
            })
            .toArray(String[]::new);
    }

    public Integer[] getBytes() {
        return Arrays.stream(getHex())
            .map(x -> Integer.valueOf(x, 16))
            .toArray(Integer[]::new);
    }

    public String[] getChars() {
        return lines.split("");
    }
}
