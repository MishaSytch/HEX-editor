package hex.editor.Frames;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private Toolkit toolkit;
    private int screenWigth;
    private int screenHeight;
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");
    
    private Color BackNotMainColor = new Color(20, 10, 15);
    private Color BackNotMainColor_Alpha = new Color(20, 10, 15, 99);
    private Color BackNotMainColor_LIGHTER = new Color(50, 42, 54);
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
        mainPanel.setLayout(new BorderLayout());
        {
            JPanel hexPanel = new JPanel();
            hexPanel.setPreferredSize(new Dimension((int)(mainPanel.getWidth() * 0.5), mainPanel.getHeight()));
            hexPanel.setBackground(Color.BLUE);
            hexPanel.setLayout(new BorderLayout());
            mainPanel.add(hexPanel, BorderLayout.WEST);

            JPanel editPanel = new JPanel();
            editPanel.setPreferredSize(new Dimension((int)(mainPanel.getWidth() * 0.5), mainPanel.getHeight()));
            editPanel.setBackground(BackNotMainColor_Alpha);
            editPanel.setLayout(new BorderLayout());
            mainPanel.add(editPanel, BorderLayout.EAST);
        }
        base.add(mainPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((int)(wigth * 0.25), height));
        leftPanel.setBackground(BackNotMainColor);
        leftPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension((int)(wigth * 0.15), height));
        rightPanel.setBackground(BackNotMainColor_Alpha);
        base.add(rightPanel, BorderLayout.EAST);

        JPanel headPanel = new JPanel();
        headPanel.setPreferredSize(new Dimension((int)(wigth * 0.06), (int)(height * 0.03)));
        headPanel.setBackground(BackNotMainColor);
        headPanel.setLayout(new BorderLayout());
        headPanel.setBorder(BorderFactory.createEtchedBorder(1));
        {
            JMenuBar menuBar = new JMenuBar();
            menuBar.setBackground(BackNotMainColor);

            JMenu fileMenu = new JMenu("File");
            JMenuItem openFile = new JMenuItem("Open file");
            fileMenu.setForeground(MainTextColor);
            JMenuItem save = new JMenuItem("Save");
            JMenuItem saveAs = new JMenuItem("Save as");
            fileMenu.add(openFile);
            fileMenu.add(save);
            fileMenu.add(saveAs);
            menuBar.add(fileMenu);

            JMenu helpMenu = new JMenu("Help");
            helpMenu.setForeground(MainTextColor);
            JMenuItem infoItem = new JMenuItem("What is Hex?");
            helpMenu.add(infoItem);
            menuBar.add(helpMenu);

            headPanel.add(menuBar, BorderLayout.WEST);
        }

        base.add(headPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension((int)(wigth * 0.15), (int)(height * 0.15)));
        bottomPanel.setBackground(BackNotMainColor_LIGHTER);
        // bottomPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(bottomPanel, BorderLayout.SOUTH);

        this.add(base);
    }
}
