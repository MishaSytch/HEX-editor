package hex.editor.view.Panel;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
                                    if (workPanel.getHex() != null) workPanel.removeFile();
                                    FileViewer.openFile(fileChooser.getSelectedFile().getAbsolutePath(), countOfColumn, countOfRows);
                                }
                                workPanel.setHex(FileViewer.getCurrentLines());
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
                        JDialog dialog = new JDialog();
                        JLabel text = new JLabel("Wait for saving file...");
                        
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() {
                                try {
                                    if (workPanel.getCurrentFile().isModified()) FileWriter.writeCacheFile(workPanel.getCurrentFile());
                                    FileWriter.saveFile(Paths.get(fileChooser.getSelectedFile().getAbsolutePath() + ".txt"));
                                } catch (Exception ex) {
                                    System.out.println("View: FileWriter error");
                                }
                                return null;
                            }
                            
                            @Override
                            protected void done() {
                                dialog.dispose();
                                JOptionPane.showMessageDialog(null, "File saved!", "Saving...", JOptionPane.INFORMATION_MESSAGE);
                            }
                        };
                        SwingWorker<Void, Void> waitWorker = new SwingWorker<Void,Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {
                                int i = 1;
                                while(!worker.isDone()) {
                                    switch (i) {
                                        case 1:
                                            text.setText("Wait for saving file.");
                                            i = 2;
                                            break;
                                        case 2:
                                            text.setText("Wait for saving file..");
                                            i = 3;
                                            break;
                                        case 3:
                                            text.setText("Wait for saving file...");
                                            i = 1;
                                            break;
                                    }
                                    Thread.sleep(1000);
                                }
                                return null;
                            }
                            
                        };
                        dialog.setModal(true);
                        dialog.setUndecorated(true);
                        dialog.setSize(200, 100);
                        dialog.setLocationRelativeTo(null);
                        dialog.setLayout(new BorderLayout());
                        dialog.add(text, BorderLayout.CENTER);
                        worker.execute();
                        waitWorker.execute();
                        dialog.setVisible(true);
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
