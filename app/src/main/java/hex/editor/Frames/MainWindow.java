package hex.editor.Frames;

import javax.swing.*;

import org.checkerframework.checker.units.qual.h;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MainWindow extends JFrame {
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private int screenWigth;
    private int screenHeight;
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");

    public MainWindow(
    ) {
        screenWigth = (int) toolkit.getScreenSize().getWidth();
        screenHeight = (int) toolkit.getScreenSize().getHeight();

        int height = (int)(screenHeight * 0.8);
        int wigth = (int)(screenWigth * 0.6);

        this.setBounds((screenWigth - wigth) / 2, (screenHeight - height) / 2, wigth, height);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(TITLE);
        this.setIconImage(icon.getImage());

        BiFunction<Double, Double, JPanel> getPanel = (XperW, YperH) -> {
            JPanel jPanel = new JPanel();
            jPanel.setSize((int)(screenWigth * XperW), (int)(screenHeight * YperH));
            return jPanel;
        };

        JPanel mainPanel = getPanel.apply((double) 1, (double) 1);
        JPanel leftPanel = getPanel.apply(0.125, (double) 1);
        JPanel rightPanel = getPanel.apply(0.125, (double) 1);

        leftPanel.setBackground(Color.BLUE);
        rightPanel.setBackground(Color.BLUE);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
    }
}
