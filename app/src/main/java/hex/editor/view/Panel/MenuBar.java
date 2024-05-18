package hex.editor.view.Panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.nio.file.Paths;
import java.util.concurrent.Exchanger;

import hex.editor.services.FileViewer;
import hex.editor.services.FileWriter;
import hex.editor.view.Panel.origin.WorkPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class MenuBar extends JMenuBar {
    private IStyleSheet styleSheet = new StyleSheet_MainWindow();
    private File file = null;
    
    public MenuBar(WorkPanel workPanel) {
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
                        System.out.println("View: load File");
                        while (true) {
                            try {
                                String textColumns = JOptionPane.showInputDialog(null, "Type count of columns:").trim();
                                int countOfColumn = Integer.parseInt(textColumns);
                                String textRows = JOptionPane.showInputDialog(null, "Type count of rows:").trim();
                                if (textRows.isEmpty()) {
                                    workPanel.setHex(FileViewer.getLines(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn));
                                    break;
                                }
                                int countOfRows = Integer.parseInt(textRows);
                                workPanel.setHex(FileViewer.getLines(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn, countOfRows));
                                break;
                            } catch (Exception ignored) {
                            }
                        }
                        workPanel.setTitle(file.getName());
                   }
                }
            });
            fileMenu.add(openFile);
        }
        {
            JMenuItem saveFile = new JMenuItem("Save file");
            saveFile.addActionListener(e -> {
                if (file != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(MenuBar.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            FileWriter.writeFileFromListOfLists(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()), workPanel.getHex());
                        } catch (Exception ex) {
                            System.out.println("View: FileWriter error");
                        }
                    }
                }
            });
            fileMenu.add(saveFile);
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
