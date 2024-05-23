package hex.editor.view;

import javax.swing.*;
import hex.editor.view.Frame.BaseFrame;
import java.awt.*;


public class MainWindow extends BaseFrame {
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static final int screenWidth = (int)toolkit.getScreenSize().getWidth();
    private static final int screenHeight = (int) toolkit.getScreenSize().getHeight();


    public MainWindow() {
        super((int)(screenHeight * 0.8), (int)(screenWidth * 0.6));
        String TITLE = "Hex editor";
        this.setTitle(TITLE);

        ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");
        this.setIconImage(icon.getImage());
        this.setBackground(super.getBackground());
        this.setForeground(super.getBackground());
        this.setBounds((screenWidth - super.getWIDTH()) / 2, (screenHeight - super.getHEIGHT()) / 2, super.getWIDTH(), super.getHEIGHT());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
