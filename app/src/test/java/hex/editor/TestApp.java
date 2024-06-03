package hex.editor;


import java.awt.EventQueue;

import org.junit.jupiter.api.Test;

import hex.editor.controller.Thread.ViewThread;


public class TestApp {
    @Test
    void appStarts() {
        EventQueue.invokeLater(() -> {
            ViewThread view = new ViewThread();
            Thread viewThread = new Thread(view);
            viewThread.start();
            System.out.println("Main");
        });
    }
}
