package hex.editor.view.Panel;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.concurrent.Exchanger;

import hex.editor.view.Panel.origin.WorkPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class MenuBar extends JMenuBar {
    private IStyleSheet styleSheet = new StyleSheet_MainWindow();
    private File file = null;
    
    public MenuBar(Exchanger<Object> fileExchanger, WorkPanel workPanel) {
        this.setBackground(styleSheet.getBackSecondaryColor());
        JMenu fileMenu = new JMenu("File");
            fileMenu.setForeground(styleSheet.getMainTextColor());
            {
                JMenuItem openFile = new JMenuItem("Open file");

                openFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showOpenDialog(MenuBar.this) == JFileChooser.APPROVE_OPTION) {
                            file = new File(fileChooser.getSelectedFile().getAbsolutePath()); 
                            
                            try {
                                workPanel.unselectCell();
                                fileExchanger.exchange(file);
                                workPanel.setTitle(file.getName());
                                System.out.println("View: sent file");
                                workPanel.showData();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    
                });

                fileMenu.add(openFile);   
            }
            this.add(fileMenu);
    }

    public IStyleSheet getStyleSheet() {
        return styleSheet;
    }
    
    public void setStyleSheet(IStyleSheet styleSheet) {
        this.styleSheet = styleSheet;
    }
}
