package hex.editor.controller.Thread;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.concurrent.Exchanger;

import javax.swing.JPanel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.MenuBar;
import hex.editor.view.Panel.origin.WorkPanel;

public class ViewThread implements Runnable {

    private File file = null;
    private MainWindow mainWindow;
    private WorkPanel originEditPanel;
    private WorkPanel editPanel;
    private JPanel baseWorkPanel;
    private MenuBar menuBar;

    private Exchanger<File> fileExchanger;
    private Exchanger<String[]> dataExchanger;

    public ViewThread(Exchanger<File> fileExchanger, Exchanger<String[]> dataExchanger) {
        this.fileExchanger = fileExchanger;
        this.dataExchanger = dataExchanger;
    }

    private void init() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(false);
        editPanel = new WorkPanel(mainWindow, dataExchanger);

        baseWorkPanel = new JPanel(new GridLayout());
        baseWorkPanel.add(editPanel, BorderLayout.CENTER);

        menuBar = new MenuBar(fileExchanger, editPanel);

        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel);
        mainWindow.setVisible(true);
    }

    @Override
    public void run() {
        init();
    }    
}
