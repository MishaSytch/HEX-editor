package hex.editor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import hex.editor.controller.HexEditor;
import hex.editor.services.HexService;
import hex.editor.services.TableViewer;
import hex.editor.view.Panel.origin.BasePanel;

import java.awt.*;
import java.io.File;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow extends JFrame {
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private int screenWidth = (int)toolkit.getScreenSize().getWidth();
    private int screenHeight = (int) toolkit.getScreenSize().getHeight();
    private String TITLE = "Hex editor";
    private ImageIcon icon = new ImageIcon("app\\src\\main\\resources\\img\\MainIcon.png");

    private HexEditor hexEditor;
    private File file;

    private JPanel editPanel;
    private JPanel hexPanel;


    public MainWindow() {
        this.setPreferredSize( 
            new Dimension(
                (int)(screenHeight * 0.8),
                (int)(screenWidth * 0.6)
            )
        );
        this.setTitle(TITLE);
        this.setIconImage(icon.getImage());
        this.setLayout(new BorderLayout());
        this.setBounds((int)((screenWidth * 0.4) / 2), (int)((screenHeight * 0.2) / 2), (int)(screenWidth * 0.6), (int)(screenHeight * 0.8));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
