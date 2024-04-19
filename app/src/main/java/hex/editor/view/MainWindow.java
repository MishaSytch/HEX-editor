package hex.editor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import hex.editor.controller.HexEditor;
import hex.editor.services.HexService;
import hex.editor.services.TableViewer;

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
    private Color BackMainColor = new Color(30, 30, 30, AlphaChanel);
    private Color BackNotMainColor = new Color(25, 25, 25, AlphaChanel);
    private Color BackNotMainColor_Alpha = new Color(20, 10, 15, AlphaChanel);
    private Color BackNotMainColor_LIGHTER = new Color(25, 15, 25, AlphaChanel);
    private Color MainTextColor = new Color(240, 240, 240);
    
    private HexEditor hexEditor;
    private File file;

    private JPanel editPanel;
    private JPanel hexPanel;


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
            hexPanel = getWorkPanel();
            hexPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
            {
                JLabel text = getText("HEX");
                text.setBorder(new EmptyBorder(10, 0, 10, 0));
                hexPanel.add(text, BorderLayout.NORTH);

                JTable table = getTable(HexService.getHexFromString("editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10"));
        
                hexPanel.add(table, BorderLayout.CENTER);
            }
            mainPanel.add(hexPanel, BorderLayout.WEST);

            editPanel = getWorkPanel();
            editPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
            {
                String info = file == null? "Pleace, open file": file.getName();
                
                JLabel text = getText(info);
                text.setBorder(new EmptyBorder(10, 0, 10, 0));
                editPanel.add(text, BorderLayout.NORTH);

                JTable table = getTable("editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10editPanel.getHeight() / 10".split(""));
                editPanel.add(table, BorderLayout.CENTER);
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

    private JTable getTable(String[] data) {
        int colomns_count = 10;

        String[] colomns = new String[colomns_count];
        for (int i = 0 ; i < colomns_count; i++) colomns[i] = String.valueOf(i);
        String[][] box = TableViewer.getTable(data, colomns_count);

        JTable table = new JTable(box, colomns);
        // Настройка таблицы
        table.setRowHeight(30);
        table.setRowHeight(1, 20);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowVerticalLines(true);
        table.setGridColor(MainTextColor);
        table.setBackground(BackMainColor);
        table.setForeground(MainTextColor);

        return table;
    }

    private JLabel getText(String info){
        JLabel text = new JLabel(info, SwingConstants.CENTER);
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
        panel.setLayout(new BorderLayout());

        JTable table = new JTable();
        table.setBorder(BorderFactory.createBevelBorder(1));
        // table.add();

        panel.add(table);

        return panel;
    }
}
