package hex.editor.view.Panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.nio.file.Paths;

import hex.editor.services.FileViewer;
import hex.editor.services.FileWriter;
import hex.editor.view.Panel.origin.WorkPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;

public class MenuBar extends JMenuBar {
    private IStyleSheet styleSheet = new StyleSheet_MainWindow();
    private File file = null;
    private final JMenuItem openFile = new JMenuItem("Open file");
    private final JMenuItem saveFile = new JMenuItem("Save file");
    
    public MenuBar(WorkPanel workPanel) {
        this.setBackground(styleSheet.getBackSecondaryColor());
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(styleSheet.getMainTextColor());
        {
            openFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser fileChooser = new JFileChooser();

                    if (fileChooser.showOpenDialog(MenuBar.this) == JFileChooser.APPROVE_OPTION) {
                        file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        System.out.println("View: load File");
                        while (true) {
                            try {
                                String textColumns = JOptionPane.showInputDialog(null, "Type count of columns:");
                                if (textColumns != null && !textColumns.trim().isEmpty()) {
                                    textColumns = textColumns.trim();
                                } else {
                                    break;
                                }
                                int countOfColumn = Integer.parseInt(textColumns);
                                String textRows = JOptionPane.showInputDialog(null, "Type count of rows:").trim();
                                if (textRows.isEmpty()) {
                                    FileViewer.openFile(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn);
                                } else {
                                    int countOfRows = Integer.parseInt(textRows);
                                    FileViewer.openFile(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn, countOfRows);
                                }
                                if (workPanel.getHex() != null) workPanel.removeFile();
                                workPanel.setHex(FileViewer.getCurrentFile());
                                saveFile.setEnabled(true);
                                break;
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                   }
                }
            });
            fileMenu.add(openFile);
        }
        {
            saveFile.addActionListener(e -> {
                if (file != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(MenuBar.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            JOptionPane.showConfirmDialog(null, "Wait for saving file", "Saving...", JOptionPane.YES_NO_OPTION);
                            if (workPanel.getCurrentFile().isModified()) FileWriter.writeCacheFile(workPanel.getCurrentFile());
                            FileWriter.saveFile(Paths.get(fileChooser.getSelectedFile().getAbsolutePath() + ".txt"));
                            JOptionPane.showConfirmDialog(null, "File saved!", "Saving...", JOptionPane.YES_NO_OPTION);
                        } catch (Exception ex) {
                            System.out.println("View: FileWriter error");
                        }
                    }
                }
            });
            fileMenu.add(saveFile);
            saveFile.setEnabled(false);
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
