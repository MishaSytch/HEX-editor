package hex.editor.Frames;

import javax.swing.*;

import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {
    private Toolkit toolkit;
    private int screenWigth;
    private int screenHeight;
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");


    private float LEFT_SIZE = 0.25f;
    private float RIGHT_SIZE = 0.15f;
    private float HEAD_SIZE = 0.04f;
    private float BOTTOM_SIZE = 0.15f;
    
    
    private Color BackMainColor = new Color(10, 10, 15);
    private Color BackNotMainColor = new Color(20, 10, 15);
    private Color BackNotMainColor_Alpha = new Color(20, 10, 15, 50);
    private Color BackNotMainColor_LIGHTER = new Color(25, 15, 25);
    private Color MainTextColor = new Color(240, 240, 240);
    
    private File file = new File("");

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
        this.setMinimumSize(new Dimension(wigth, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JPanel base = new JPanel(new BorderLayout());
        base.setBounds(new Rectangle(new Dimension(height, wigth)));

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((int)(this.getWidth() * LEFT_SIZE), height));
        leftPanel.setBackground(BackNotMainColor);
        leftPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(leftPanel, BorderLayout.WEST);

        JPanel headPanel = new JPanel();
        headPanel.setPreferredSize(new Dimension((int)(wigth), (int)(this.getHeight() * HEAD_SIZE)));
        headPanel.setBackground(BackNotMainColor);
        headPanel.setLayout(new BorderLayout());
        {
            JMenuBar menuBar = new JMenuBar();
            menuBar.setBackground(BackNotMainColor);

            JMenu fileMenu = new JMenu("File");
            fileMenu.setForeground(MainTextColor);
            {
                JMenuItem openFile = new JMenuItem("Open file");
                JMenuItem save = new JMenuItem("Save");
                JMenuItem saveAs = new JMenuItem("Save as");

                fileMenu.add(openFile);
                fileMenu.add(save);
                fileMenu.add(saveAs);    
            }
            menuBar.add(fileMenu);

            JMenu helpMenu = new JMenu("Help");
            helpMenu.setForeground(MainTextColor);
            {
                JMenuItem infoItem = new JMenuItem("What is Hex?");

                helpMenu.add(infoItem);    
            }
            menuBar.add(helpMenu);

            headPanel.add(menuBar, BorderLayout.WEST);
        }

        base.add(headPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();        
        mainPanel.setPreferredSize(new Dimension(wigth, height));
        mainPanel.setBackground(Color.lightGray);
        mainPanel.setLayout(new GridLayout());
        {
            JPanel hexPanel = new JPanel();
            hexPanel.setBackground(BackMainColor);
            hexPanel.setPreferredSize(new Dimension((int)(this.getWidth() * (0.5 - LEFT_SIZE)), this.getHeight()));
            hexPanel.setBorder(BorderFactory.createEtchedBorder(1));
            hexPanel.setForeground(MainTextColor);
            hexPanel.setLayout(new FlowLayout());
            {
                JLabel text = new JLabel();
                text.setText("HEX");
                text.setForeground(MainTextColor);
                hexPanel.add(text);
            }

            JPanel editPanel = new JPanel();
            editPanel.setBackground(BackMainColor);
            editPanel.setPreferredSize(new Dimension((int)(this.getWidth() * (0.5 - LEFT_SIZE)), this.getHeight()));
            editPanel.setBorder(BorderFactory.createEtchedBorder(1));
            editPanel.setForeground(MainTextColor);
            editPanel.setLayout(new FlowLayout());
            {
                JLabel text = new JLabel();
                text.setText(file != null? "Pleace, open file!": file.getName());
                text.setForeground(MainTextColor);
                editPanel.add(text);
            }

            mainPanel.add(hexPanel, BorderLayout.WEST);
            mainPanel.add(editPanel, BorderLayout.EAST);
        }
        base.add(mainPanel, BorderLayout.CENTER);

        this.add(base);
    }
}
