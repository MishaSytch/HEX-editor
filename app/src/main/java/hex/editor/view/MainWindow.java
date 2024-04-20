package hex.editor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import hex.editor.controller.HexEditor;
import hex.editor.services.HexService;
import hex.editor.services.TableViewer;
import hex.editor.view.Frame.BaseFrame;
import hex.editor.view.Panel.origin.BasePanel;

import java.awt.*;
import java.io.File;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
