package hex.editor.controller;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.MenuBar;
import hex.editor.view.Panel.origin.WorkPanel;

public class ViewController {
    public ViewController() {
        MainWindow mainWindow = new MainWindow();
        WorkPanel originEditPanel = new WorkPanel(500, mainWindow.getWidth());
        WorkPanel hexEditPanel = new WorkPanel(0, 0);
        WorkPanel[] workPanels = new WorkPanel[] {
            originEditPanel,
            hexEditPanel
        };
        JPanel baseWorkPanel = new JPanel(new GridLayout());
        baseWorkPanel.add(hexEditPanel, BorderLayout.WEST);
        baseWorkPanel.add(originEditPanel, BorderLayout.EAST);

        MenuBar menuBar = new MenuBar(workPanels);

        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel);
        mainWindow.setVisible(true);
    }
}
