package hex.editor.view;

import javax.swing.*;
import hex.editor.view.Frame.BaseFrame;
import java.awt.*;


public class MainWindow extends BaseFrame {
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static int screenWidth = (int)toolkit.getScreenSize().getWidth();
    private static int screenHeight = (int) toolkit.getScreenSize().getHeight();
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");


    public MainWindow() {
        super((int)(screenHeight * 0.8), (int)(screenWidth * 0.6));
        this.setTitle(TITLE);
        this.setIconImage(icon.getImage());
        this.setBounds((screenWidth - super.getWIDTH()) / 2, (screenHeight - super.getHEIGHT()) / 2, super.getWIDTH(), super.getHEIGHT());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
