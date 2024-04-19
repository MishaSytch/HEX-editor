package hex.editor.controller;

import java.util.stream.Collectors;
import java.util.Arrays;

import hex.editor.services.FileViewer;
import hex.editor.services.HexService;

public class HexEditor {
    private String strings;

    public String getStrings() {
        return strings;
    }

    public HexEditor(String path) {
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public void openNewFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public String[] getHexString() {
        return HexService.getHexFromString(strings);
    }

    public String[] getCharsString() {
        return HexService.getCharsFromString(strings);
    }

    public void editOpenedFileByHex(String[] hex) {
        strings = Arrays.stream(HexService.getCharsFromHex(hex)).collect(Collectors.joining(", "));
    } 

    public void editOpenedFileByChars(String[] chars) {
        strings = Arrays.stream(chars).collect(Collectors.joining(", "));
    } 
}
