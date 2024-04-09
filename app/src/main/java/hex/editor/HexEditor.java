package hex.editor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hex.editor.Frames.MainWindow;

public class HexEditor {
    public HexEditor() {
        EventQueue.invokeLater(() -> {
            JFrame mainFrame = new MainWindow();
        });}
}
