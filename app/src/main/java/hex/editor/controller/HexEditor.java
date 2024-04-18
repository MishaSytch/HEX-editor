package hex.editor.controller;

import hex.editor.services.HEXservice;

public class HexEditor {
    private HEXservice hexService;

    public HexEditor() {
        hexService = new HEXservice();
    }
}
