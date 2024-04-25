package hex.editor.controller.Thread;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.concurrent.Exchanger;

import javax.swing.SwingUtilities;

import javax.swing.JPanel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import hex.editor.view.Panel.MenuBar;
import hex.editor.view.Panel.origin.WorkPanel;

public class ViewThread implements Runnable {
    private MainWindow mainWindow;
    private WorkPanel editPanel;
    private InfoPanel infoPanel;
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
        infoPanel = new InfoPanel(mainWindow);
        editPanel = new WorkPanel(mainWindow, infoPanel, dataExchanger);

        baseWorkPanel = new JPanel(new BorderLayout());
        baseWorkPanel.add(editPanel, BorderLayout.CENTER);
        baseWorkPanel.add(infoPanel, BorderLayout.WEST);
        

        menuBar = new MenuBar(fileExchanger, editPanel);

        mainWindow.add(menuBar, BorderLayout.NORTH);
        mainWindow.add(baseWorkPanel, BorderLayout.CENTER);

        mainWindow.addComponentListener(new ComponentListener(){
            @Override
            public void componentResized(ComponentEvent event) {
                
                SwingUtilities.updateComponentTreeUI(baseWorkPanel);
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {}

            @Override
            public void componentMoved(ComponentEvent arg0) {}

            @Override
            public void componentShown(ComponentEvent arg0) {}

        });

        mainWindow.setVisible(true);
    }

    @Override
    public void run() {
        init();
    }    
}
