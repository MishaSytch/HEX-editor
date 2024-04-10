package hex.editor.Frames;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private Toolkit toolkit;
    private int screenWigth;
    private int screenHeight;
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");
    
    private Color BackNotMainColor = new Color(20, 10, 15);
    private Color MainTextColor = new Color(240, 240, 240);
    

    public MainWindow(
    ) {
        toolkit = Toolkit.getDefaultToolkit();
        screenWigth = (int) toolkit.getScreenSize().getWidth();
        screenHeight = (int) toolkit.getScreenSize().getHeight();

        int height = (int)(screenHeight * 0.8);
        int wigth = (int)(screenWigth * 0.6);

        this.setTitle(TITLE);
        this.setIconImage(icon.getImage());
        this.setLayout(new BorderLayout());
        this.setBounds((screenWigth - wigth) / 2, (screenHeight - height) / 2, wigth, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JPanel base = new JPanel(new BorderLayout());
        base.setBounds(new Rectangle(new Dimension(height, wigth)));

        JPanel mainPanel = new JPanel();        
        mainPanel.setPreferredSize(new Dimension(wigth, height));
        mainPanel.setBackground(Color.lightGray);
        base.add(mainPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((int)(wigth * 0.20), height));
        leftPanel.setBackground(BackNotMainColor);
        leftPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension((int)(wigth * 0.20), height));
        rightPanel.setBackground(BackNotMainColor);
        rightPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(rightPanel, BorderLayout.EAST);

        JPanel headPanel = new JPanel();
        headPanel.setPreferredSize(new Dimension(wigth, (int)(height * 0.03)));
        headPanel.setBackground(BackNotMainColor);
        headPanel.setLayout(new BorderLayout());
        headPanel.setBorder(BorderFactory.createEtchedBorder(1));

        base.add(headPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(wigth, (int)(height * 0.15)));
        bottomPanel.setBackground(BackNotMainColor);
        // bottomPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(bottomPanel, BorderLayout.SOUTH);

        this.add(base);
    }
}
