package hex.editor.view.Panel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;
import hex.editor.view.Panel.origin.WorkPanel;

import javax.swing.*;
import java.awt.event.*;

public class InfoPanel extends BasePanel {
    private JLabel mainInfo;


    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));

        mainInfo = new JLabel();
    }

    public void setPosition(int row, int column) {
        JLabel row_Info = getText(String.valueOf(row));
        JLabel column_Info = getText(String.valueOf(column));

        System.out.println(row + " " + column);

        mainInfo.setText(String.valueOf(row));
        mainInfo.add(column_Info);        
    }

    public void setValueInfo(String ch, String hex) {
        mainInfo.setText("Char: " + ch);
        mainInfo.setText("Hex: " + hex);

        System.out.println(ch + " " + hex);
    }

}
