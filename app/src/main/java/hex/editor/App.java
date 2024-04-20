package hex.editor;


import java.awt.EventQueue;
import javax.swing.JFrame;

import hex.editor.view.MainWindow;
import hex.editor.view.ViewController;

public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(() -> {
            ViewController ViewController = new ViewController();
        });
    }
}
