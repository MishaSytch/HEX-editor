package hex.editor.view.Panel;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import hex.editor.controller.HexEditor;
import hex.editor.view.Panel.origin.WorkPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class MenuBar extends JMenuBar implements ActionListener {
    private IStyleSheet styleSheet = new StyleSheet_MainWindow();
    private File file = null;
    private WorkPanel hexWorkPanel;
    private WorkPanel originWorkPanel;

    public File getFile() {
        return file;
    }
    
    public MenuBar(WorkPanel hexWorkPanel, WorkPanel originWorkPanel) {
        this.hexWorkPanel = hexWorkPanel;
        this.originWorkPanel = originWorkPanel;

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

                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                            @SuppressWarnings("rawtypes")
                            SwingWorker swingWorker = new SwingWorker<Boolean,String[]>() {
                                @Override
                                protected Boolean doInBackground() throws Exception { 
                                    HexEditor hexEditor = new HexEditor(file.getAbsolutePath());

                                    String[] hex = hexEditor.getHexString();
                                    String[] chars = hexEditor.getCharsString();
                                    
                                    hexWorkPanel.showData(hex);
                                    originWorkPanel.showData(chars);

                                    return null;
                                }

                                @Override
                                protected void done() {

                                }            
                            };

                            swingWorker.execute();
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
