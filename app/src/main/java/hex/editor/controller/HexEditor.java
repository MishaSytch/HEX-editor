package hex.editor.controller;

import java.util.stream.Collectors;

import hex.editor.services.FileViewer;
import hex.editor.services.HEXservice;

public class HexEditor {
    private HEXservice hexService;
    String lines;

    public HexEditor() {
        hexService = new HEXservice();
    }

    public void readFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        lines = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }
}
