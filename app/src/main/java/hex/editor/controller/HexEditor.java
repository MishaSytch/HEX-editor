package hex.editor.controller;

import java.util.stream.Collectors;
import java.util.Arrays;

import hex.editor.services.FileViewer;
import hex.editor.services.HexService;

public class HexEditor {
    private String string;

    public String getString() {
        return string;
    }

    public HexEditor(String path) {
        FileViewer fileViewer = new FileViewer(path);
        string = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public void openNewFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        string = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public String[] getHexString() {
        return HexService.getHexFromString(string);
    }

    public String[] getCharsString() {
        return HexService.getCharsFromString(string);
    }

    public void editOpenedFileByHex(String[] hex) {
        string = Arrays.stream(HexService.getCharsFromHex(hex)).collect(Collectors.joining(""));
    } 

    public void editOpenedFileByChars(String[] chars) {
        string = Arrays.stream(chars).collect(Collectors.joining(""));
    } 
}
