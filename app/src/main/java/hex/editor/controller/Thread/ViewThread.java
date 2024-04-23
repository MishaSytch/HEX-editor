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

    private ServiceThread service;


    public ViewThread() {
        init();
    }

    public void init() {
        mainWindow = new MainWindow();
        editPanel = new WorkPanel(mainWindow.getHeight(), mainWindow.getWidth());

        baseWorkPanel = new JPanel(new GridLayout());
        baseWorkPanel.add(editPanel, BorderLayout.CENTER);

        menuBar = new MenuBar(baseWorkPanel, mainWindow);

        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel);
        mainWindow.setVisible(true);

        service = new ServiceThread(fileExchanger, dataExchanger);
    }

    @Override
    public void run() {
        while (true) {
            file = menuBar.getFile();  
            if (file != null) {
                try {
                    service.run();
                    fileExchanger.exchange(file);

                    String[] hex = new String[]{""};
                    String[] chars = new String[]{""};

                    hex = dataExchanger.exchange(hex);
                    chars = dataExchanger.exchange(chars);

                    originEditPanel.showData(chars);
                    editPanel.showData(hex);

                    originEditPanel.repaint();
                    editPanel.repaint();

                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    };
    
}
