package hex.editor.controller;

import java.util.stream.Collectors;

import hex.editor.services.FileViewer;
import hex.editor.services.HEXservice;

public class HexEditor {
    private HEXservice hexService;
    private String strings;

    public HexEditor(String path) {
        hexService = new HEXservice();
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public void openNewFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public String[] getHexString() {
        return hexService.getHexFromString(strings);
    }

    public String[] getCharsString() {
        return hexService.getCharsFromString(strings);
    }

    public void editOpenedFileByHex(String[] hex) {
        String.join(strings, hexService.getCharsFromHex(hex));
    } 

    public void editOpenedFileByChars(String[] chars) {
        String.join(strings, chars);
    } 
}
