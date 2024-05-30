package hex.editor;


import java.awt.EventQueue;

import hex.editor.controller.Thread.ViewThread;


public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(App::setup);
    }

    private static void setup() {
        ViewThread view = new ViewThread();
        Thread viewThread = new Thread(view);
        viewThread.start();
        System.out.println("Main");
    }
}
