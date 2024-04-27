package hex.editor.controller.Thread;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;
import java.util.concurrent.Exchanger;

import javax.swing.SwingUtilities;

import javax.swing.JPanel;

import hex.editor.model.Types;
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
    private Map<Types, Exchanger<Object>> exchangers;



    public ViewThread(Map<Types, Exchanger<Object>> exchangers) {
        this.exchangers = exchangers;
    }

    private void init() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(false);
        infoPanel = new InfoPanel(mainWindow, exchangers);
        editPanel = new WorkPanel(mainWindow, infoPanel, exchangers);
        infoPanel.setWorkPanel(editPanel);

        baseWorkPanel = new JPanel(new BorderLayout());
        baseWorkPanel.add(editPanel, BorderLayout.CENTER);
        baseWorkPanel.add(infoPanel, BorderLayout.WEST);
        

        menuBar = new MenuBar(exchangers.get(Types.FILE), editPanel);

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
