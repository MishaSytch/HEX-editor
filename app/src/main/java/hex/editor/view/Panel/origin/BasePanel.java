package hex.editor.view.Panel.origin;

import java.awt.*;
import javax.swing.*;

import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class BasePanel extends JPanel {
    private final IStyleSheet styleSheet = new StyleSheet_MainWindow();

    public IStyleSheet getStyleSheet() {
        return styleSheet;
    }

    public BasePanel(int height, int width) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getForeBaseColor());
    }

    public JLabel getText(String info){
        JLabel text = new JLabel(info, SwingConstants.CENTER);
        text.setForeground(styleSheet.getMainTextColor());
        return text;
    }
}
