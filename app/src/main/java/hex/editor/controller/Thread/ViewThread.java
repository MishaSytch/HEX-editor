package hex.editor.controller.Thread;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import hex.editor.view.Panel.MenuBar;
import hex.editor.view.Panel.origin.WorkPanel;

public class ViewThread implements Runnable {

    private void init() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(false);
        InfoPanel infoPanel = new InfoPanel(mainWindow);
        WorkPanel editPanel = new WorkPanel(mainWindow, infoPanel);
        infoPanel.setWorkPanel(editPanel);

        JPanel baseWorkPanel = new JPanel(new BorderLayout());
        baseWorkPanel.add(editPanel, BorderLayout.CENTER);
        baseWorkPanel.add(infoPanel, BorderLayout.WEST);

        MenuBar menuBar = new MenuBar(editPanel);

        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel, BorderLayout.CENTER);

        mainWindow.setVisible(true);
    }

    @Override
    public void run() {
        init();
    }    
}
