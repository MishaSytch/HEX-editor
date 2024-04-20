package hex.editor.view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import hex.editor.view.Panel.MenuBar;
import hex.editor.view.Panel.origin.WorkPanel;

public class ViewController {
    public ViewController() {
        MainWindow mainWindow = new MainWindow();
        MenuBar menuBar = new MenuBar();
        WorkPanel originEditPanel = new WorkPanel(0, 0);
        WorkPanel hexEditPanel = new WorkPanel(0, 0);
        JPanel baseWorkPanel = new JPanel(new GridLayout());
        baseWorkPanel.add(hexEditPanel, BorderLayout.WEST);
        baseWorkPanel.add(originEditPanel, BorderLayout.EAST);
        
        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel);
        mainWindow.setVisible(true);
    }
    
}
