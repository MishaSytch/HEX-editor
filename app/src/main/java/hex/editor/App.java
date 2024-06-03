package hex.editor;


import java.awt.EventQueue;

import hex.editor.controller.Thread.ViewThread;
import hex.editor.services.FileViewer;


public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(App::setup);
    }

    private static void setup() {
        ViewThread view = ViewThread.getInstance();
        
        System.out.println("Main");
    }
}
