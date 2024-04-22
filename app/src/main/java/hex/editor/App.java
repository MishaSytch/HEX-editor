package hex.editor;


import java.awt.EventQueue;

import hex.editor.controller.Controller;

public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(() -> {
            Controller controller = new Controller();
        });
    }
}
