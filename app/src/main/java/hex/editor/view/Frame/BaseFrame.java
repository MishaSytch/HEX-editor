package hex.editor.view.Frame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class BaseFrame extends JFrame {

    private final int HEIGHT;
    public int getHEIGHT() {
        return HEIGHT;
    }
    private final int WIDTH;
    public int getWIDTH() {
        return WIDTH;
    } 

    public BaseFrame(int height, int width) {
        HEIGHT = height;
        WIDTH = width;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        IStyleSheet styleSheet = new StyleSheet_MainWindow();
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getForeBaseColor());
        this.setLayout(new BorderLayout());
    }
    
}
