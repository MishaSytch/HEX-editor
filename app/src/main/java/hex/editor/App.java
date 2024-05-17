package hex.editor;


import java.awt.EventQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

import hex.editor.controller.Thread.ViewThread;
import hex.editor.model.Types;


public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(() -> setup());
    }

    private static void setup() {
        ViewThread view = new ViewThread();
        Thread viewThread = new Thread(view);
        viewThread.start();

        System.out.println("Main");
    }
}
