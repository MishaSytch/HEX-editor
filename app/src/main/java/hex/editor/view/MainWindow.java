package hex.editor.view;

import javax.swing.*;

import hex.editor.controller.HexEditor;

import java.awt.*;
import java.io.File;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow extends JFrame implements ActionListener {
    private Toolkit toolkit;
    private int screenWigth;
    private int screenHeight;
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");


    private float LEFT_SIZE = 0.25f;
    private float RIGHT_SIZE = 0.15f;
    private float HEAD_SIZE = 0.04f;
    private float BOTTOM_SIZE = 0.15f;
    
    private int AlphaChanel = 255;
    private Color BackMainColor = new Color(10, 10, 15, AlphaChanel);
    private Color BackNotMainColor = new Color(20, 10, 15, AlphaChanel);
    private Color BackNotMainColor_Alpha = new Color(20, 10, 15, AlphaChanel);
    private Color BackNotMainColor_LIGHTER = new Color(25, 15, 25, AlphaChanel);
    private Color MainTextColor = new Color(240, 240, 240);
    
    private HexEditor hexEditor;
    private File file;


    public MainWindow(
    ) {
        toolkit = Toolkit.getDefaultToolkit();
        screenWigth = (int) toolkit.getScreenSize().getWidth();
        screenHeight = (int) toolkit.getScreenSize().getHeight();

        int height = (int)(screenHeight * 0.8);
        int wigth = (int)(screenWigth * 0.6);

        this.setTitle(TITLE);
        this.setForeground(BackMainColor);
        this.setIconImage(icon.getImage());
        this.setLayout(new BorderLayout());
        this.setBounds((screenWigth - wigth) / 2, (screenHeight - height) / 2, wigth, height);
        this.setMinimumSize(new Dimension(wigth, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel base = getMainPanel(1, 1, Color.WHITE);
        
        JPanel leftPanel = getMainPanel(LEFT_SIZE, 1, BackNotMainColor);
        leftPanel.setBorder(BorderFactory.createEtchedBorder(1));
        base.add(leftPanel, BorderLayout.WEST);

        JPanel headPanel = getMainPanel(1, HEAD_SIZE, BackNotMainColor);
        {
            JMenuBar menuBar = new JMenuBar();
            menuBar.setBackground(BackNotMainColor);

            JMenu fileMenu = new JMenu("File");
            fileMenu.setForeground(MainTextColor);
            {
                JMenuItem openFile = new JMenuItem("Open file");
                JMenuItem save = new JMenuItem("Save");
                JMenuItem saveAs = new JMenuItem("Save as");

                openFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                            hexEditor = new HexEditor(file.getAbsolutePath());
                        }
                    }
                });

                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                             if (file != null) {
                                
                             }  else {

                             }
                        }
                    }
                });
                
                saveAs.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                             if (file != null) {
                                String path = fileChooser.getSelectedFile().getAbsolutePath();
                                String name = fileChooser.getSelectedFile().getName();
                                
                             }  else {

                             }
                        }
                    }
                });

                fileMenu.add(openFile);
                fileMenu.add(save);
                fileMenu.add(saveAs);    
            }
            menuBar.add(fileMenu);

            JMenu helpMenu = new JMenu("Help");
            helpMenu.setForeground(MainTextColor);
            {
                JMenuItem infoItem = new JMenuItem("What is Hex?");

                infoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                });

                helpMenu.add(infoItem);    
            }
            menuBar.add(helpMenu);

            headPanel.add(menuBar, BorderLayout.WEST);
        }
        base.add(headPanel, BorderLayout.NORTH);

        JPanel mainPanel = getMainPanel(1, 1, Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridLayout());
        {
            JPanel hexPanel = getWorkPanel();
            {
                JLabel text = getText("HEX");
                hexPanel.add(text, BorderLayout.NORTH);
            }
            mainPanel.add(hexPanel, BorderLayout.WEST);

            JPanel editPanel = getWorkPanel();
            {
                String info = file == null? "Pleace, open file": file.getName();
                JLabel text = getText(info);
                editPanel.add(text, BorderLayout.NORTH);
            }
            mainPanel.add(editPanel, BorderLayout.EAST);
        }
        base.add(mainPanel, BorderLayout.CENTER);

        this.add(base);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
    }

    private JLabel getText(String info){
        JLabel text = new JLabel();
        text.setText(info);
        text.setForeground(MainTextColor);
        return text;
    }

    private JPanel getMainPanel(double perWidth, double perHeight, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int)(this.getWidth() * perWidth), (int)(this.getHeight() * perHeight)));
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel getWorkPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BackMainColor);
        panel.setPreferredSize(new Dimension((int)(this.getWidth() * (0.5 - LEFT_SIZE)), this.getHeight()));
        panel.setBorder(BorderFactory.createEtchedBorder(1));
        panel.setForeground(MainTextColor);
        panel.setLayout(new FlowLayout());

        JTable table = new JTable();
        table.setBorder(BorderFactory.createBevelBorder(1));
        // table.add();

        panel.add(table);

        return panel;
    }
}
