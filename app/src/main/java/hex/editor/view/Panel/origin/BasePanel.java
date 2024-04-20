package hex.editor.view.Panel.origin;

import java.awt.*;
import javax.swing.*;

import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class BasePanel extends JPanel {
    protected IStyleSheet styleSheet = new StyleSheet_MainWindow();

    public IStyleSheet getStyleSheet() {
        return styleSheet;
    }

    public void setStyleSheet(IStyleSheet styleSheet) {
        this.styleSheet = styleSheet;
    }

    private int HEIGHT;
    public int getHEIGHT() {
        return HEIGHT;
    }
    private int WIDTH;
    public int getWIDTH() {
        return WIDTH;
    }   

    public BasePanel(int height, int width) {
        HEIGHT = height;
        WIDTH = width;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getForeBaseColor());
    }

    public JLabel getText(String info){
        JLabel text = new JLabel(info, SwingConstants.CENTER);
        text.setForeground(styleSheet.getMainTextColor());
        return text;
    }
}
