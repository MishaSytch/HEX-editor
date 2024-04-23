package hex.editor.view.Panel;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import hex.editor.controller.HexEditor;
import hex.editor.services.Types;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.WorkPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class MenuBar extends JMenuBar implements ActionListener {
    private IStyleSheet styleSheet = new StyleSheet_MainWindow();
    private File file = null;
    private WorkPanel editPanel;
    private MainWindow mainWindow;
    private JPanel baseWorkPanel;

    public File getFile() {
        return file;
    }
    
    public MenuBar(JPanel baseWorkPanel, MainWindow mainWindow) {
        this.baseWorkPanel = baseWorkPanel;
        this.editPanel = (WorkPanel) baseWorkPanel.getComponent(0);
        this.mainWindow = mainWindow;

        this.setBackground(styleSheet.getBackSecondaryColor());
        JMenu fileMenu = new JMenu("File");
            fileMenu.setForeground(styleSheet.getMainTextColor());
            {
                JMenuItem openFile = new JMenuItem("Open file");
                JMenuItem save = new JMenuItem("Save");
                JMenuItem saveAs = new JMenuItem("Save as");

                openFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showOpenDialog(MenuBar.this) == JFileChooser.APPROVE_OPTION) {
                            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                            
                            HexEditor hexEditor = new HexEditor(file.getAbsolutePath());
                            Map<Types, String[]> map = new HashMap<Types, String[]>();

                            String[] hex = hexEditor.getHexString();
                            String[] chars = hexEditor.getCharsString();

                            map.put(Types.HEX, hex);
                                    map.put(Types.CHARS, chars);

                            mainWindow.getContentPane().remove(baseWorkPanel);

                            editPanel.showData(map.get(Types.HEX));
                            editPanel.setTitle(file.getName());

                            baseWorkPanel.remove(0);
                            baseWorkPanel.add(editPanel, BorderLayout.CENTER);

                            mainWindow.add(baseWorkPanel);
                            mainWindow.revalidate();
                            mainWindow.repaint();
                            mainWindow.getContentPane().setVisible(false);
                            mainWindow.getContentPane().setVisible(true);
                        }
                    }
                    
                });

                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        }
                    }
                });
                
                saveAs.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();

                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                String path = fileChooser.getSelectedFile().getAbsolutePath();
                                String name = fileChooser.getSelectedFile().getName();
                        }
                    }
                });

                fileMenu.add(openFile);
                fileMenu.add(save);
                fileMenu.add(saveAs);    
            }
            this.add(fileMenu);

            JMenu helpMenu = new JMenu("Help");
            helpMenu.setForeground(styleSheet.getMainTextColor());
            {
                JMenuItem infoItem = new JMenuItem("What is Hex?");

                infoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                });

                helpMenu.add(infoItem);    
            }
            this.add(helpMenu);
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    public IStyleSheet getStyleSheet() {
        return styleSheet;
    }
    
    public void setStyleSheet(IStyleSheet styleSheet) {
        this.styleSheet = styleSheet;
    }
}
