package hex.editor.view.Panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                                if (textColumns != null) {
                                    textColumns = textColumns.trim();
                                } else {
                                    break;
                                }
                                int countOfColumn = Integer.parseInt(textColumns);
                                String textRows = JOptionPane.showInputDialog(null, "Type count of rows:").trim();
                                if (textRows.isEmpty()) {
                                    workPanel.setHex(FileViewer.getLines(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn));
                                    break;
                                }
                                int countOfRows = Integer.parseInt(textRows);
                                workPanel.setHex(FileViewer.getLines(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn, countOfRows));
                                workPanel.setTitle(file.getName());
                                break;
                            } catch (Exception ignored) {
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
                            FileWriter.writeFileFromListOfLists(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()), workPanel.getHex());
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

        fileMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveFile.setEnabled(file != null);
            }
        });
    }

    public IStyleSheet getStyleSheet() {
        return styleSheet;
    }
    
    public void setStyleSheet(IStyleSheet styleSheet) {
        this.styleSheet = styleSheet;
    }
}
