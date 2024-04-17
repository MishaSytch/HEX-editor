package hex.editor;

import java.awt.EventQueue;
import javax.swing.JFrame;
import hex.editor.Frames.MainWindow;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame mainFrame = new MainWindow();
            mainFrame.repaint();
        });
    }
}
