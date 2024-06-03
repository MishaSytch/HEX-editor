package hex.editor.view.Panel.origin;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheFile;
import hex.editor.model.Info;
import hex.editor.model.Position;
import hex.editor.model.Positions;
import hex.editor.services.FileViewer;
import hex.editor.services.FileWriter;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import hex.editor.view.Style.IStyleSheet;

public class WorkPanel extends BasePanel {
    private final JTable table = new JTable(){
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getPreferredSize().width < getParent().getWidth();
        }
    };
    private final IStyleSheet styleSheet = super.getStyleSheet();
    private final JScrollPane pane = new JScrollPane();
    private final JLabel fileName = getText("");
    private List<List<String>> hex;
    private final InfoPanel infoPanel;
    private final Positions positions = new Positions();
    private final DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JToolTip tooltip = new JToolTip();
    private Integer lastX;
    private Integer lastY;
    private final PopupFactory popupFactory = PopupFactory.getSharedInstance();
    private Popup popup;
    private Timer scopeTimer;
    private String title;
    private int lengthOfPosition;
    private boolean isModified = false;

    public CacheFile getCurrentFile() {
        return currentFile;
    }

    private CacheFile currentFile;
    private final JPanel buttons = new JPanel();
    private final JLabel currentPage = new JLabel();

    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth()*0.8));
        this.infoPanel = infoPanel;

        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.setLayout(new BorderLayout());
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getBackBaseColor());

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
                c.setBackground(styleSheet.getBackBaseColor());
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                c.setEnabled(columnIndex > 0);
                if (!positions.isEmpty() && columnIndex > 0) {
                        if (lengthOfPosition > 1) {
                            for (Position position : positions.getCurrent(lengthOfPosition)) {
                                if (position.getRow() == rowIndex && position.getColumn() + 1 == columnIndex) {
                                    c.setBackground(styleSheet.getSelectedColor());
                                    break;
                                }
                            }
                        } else {
                            if (positions.getCurrent().getRow() == rowIndex && positions.getCurrent().getColumn() + 1 == columnIndex) {
                                c.setBackground(styleSheet.getSelectedColor());
                            }
                        }
                }

                if (isSelected) {
                    c.setBackground(styleSheet.getSelectedColor());
                }
                return c;
            }
        });
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int[] selectedRow = table.getSelectedRows();
                int[] selectedColumn = table.getSelectedColumns();
                showInfoInCells(selectedRow, selectedColumn);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                if (table.getSelectedColumn() > 0) {
                    System.out.println("Info: wait info");
                    int row = table.rowAtPoint(event.getPoint());
                    int column = table.columnAtPoint(event.getPoint());
                    if (column == 0 || model.getValueAt(row, column) == null) {
                        return;
                    }
                    Info info = createInfo(event);
                    loadInfo(model, row, column, info);
                }

                if (event.getClickCount() == 2) {
                    int row = table.rowAtPoint(event.getPoint());
                    int column = table.columnAtPoint(event.getPoint());
                    if (column != 0) {
                        String text = JOptionPane.showInputDialog(null, "Edit cell:").trim();
                        if (validateData(text)) {
                            model.setValueAt(text.toUpperCase(), row, column);
                            isModified = true;
                            currentFile.updateData(getHex());
                        } else {
                            JOptionPane.showConfirmDialog(null, "Not valid!");
                        }
                    }
                }
            }
        });

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                int row = table.rowAtPoint(event.getPoint());
                int column = table.columnAtPoint(event.getPoint());

                if (column == 0 || model.getValueAt(row, column) == null) return;

                showPopup(event, createInfo(event));
            }
        });

        table.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_C) {
                    int[] selectedRows = table.getSelectedRows();
                    int[] selectedColumns = table.getSelectedColumns();
                    copyToClipBoard(model, selectedRows, selectedColumns);
                }

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_X) {
                    int[] selectedRows = table.getSelectedRows();
                    int[] selectedColumns = table.getSelectedColumns();
                    int result = JOptionPane.showConfirmDialog(null, "Do you want to cut with shift", "Cut", JOptionPane.YES_NO_OPTION);
                    cutToClipboard(model, selectedRows, selectedColumns, result == JOptionPane.YES_OPTION);
                }

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_V) {
                    try {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        String data = (String) clipboard.getData(DataFlavor.stringFlavor);
                        String[] values = data.replace("\n", "").replace("\t", ";").split(";");
                        if (validateData(data) || validateDataArray(values)) {
                            int[] selectedRows = table.getSelectedRows();
                            int[] selectedColumns = table.getSelectedColumns();
                            int valueIndex = 0;

                            int result = JOptionPane.showConfirmDialog(null, "Do you want to insert with shift", "Insert", JOptionPane.YES_NO_OPTION);
                            insertToModel(model, values, selectedRows, selectedColumns, valueIndex, result == JOptionPane.YES_OPTION);
                            System.out.println("Data pasted with validation");
                        } else {
                            System.out.println("Invalid data");
                        }
                    } catch (UnsupportedFlavorException | IOException e) {
                        System.out.println("Failed to paste data: " + e.getMessage());
                    }
                }

                if (arg0.getKeyCode() == KeyEvent.VK_DELETE) {
                    int[] selectedRows = table.getSelectedRows();
                    int[] selectedColumns = table.getSelectedColumns();
                    if (selectedRows.length > 0 && selectedColumns.length > 0) {
                        int result = JOptionPane.showConfirmDialog(null, "Do you want to delete selected cells with shift", "Delete", JOptionPane.YES_NO_OPTION);
                        deleteFromModel(model, selectedRows, selectedColumns, result == JOptionPane.YES_OPTION);
                        System.out.println("View: deleted selected cells");
                    }
                }
            }
        });

        pane.setVisible(false);
        this.add(pane, BorderLayout.CENTER);
        this.add(fileName, BorderLayout.NORTH);

        buttons.setLayout(new FlowLayout());
        JButton previousPage = new JButton("Previous page");
        buttons.add(previousPage);
        buttons.add(currentPage);
        JButton nextPage = new JButton("Next page");
        buttons.add(nextPage);
        buttons.setVisible(false);
        buttons.setBackground(getBackground());
        currentPage.setBackground(getBackground());
        currentPage.setForeground(styleSheet.getMainTextColor());
        nextPage.setBackground(getBackground());
        nextPage.setForeground(getStyleSheet().getMainTextColor());
        nextPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentFile.getIndex() != FileViewer.getSize()) loadNextFile();
            }
        });
        previousPage.setBackground(getBackground());
        previousPage.setForeground(getStyleSheet().getMainTextColor());
        previousPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentFile.getIndex() != 0) loadPreviousFile();
            }
        });
        this.add(buttons, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    public List<List<String>> getHex() {
        if (currentFile != null && isModified) {
            List<List<String>> hex = new LinkedList<>();
            for (Object line : model.getDataVector()) {
                List<String> row = new LinkedList<>();
                for (int i = 1; i < ((Vector<String>)line).size(); i++) row.add(((Vector<String>)line).get(i));
                hex.add(row);
            }
            return hex;
        }
        return hex;
    }

    public void setHex(CacheFile fileLines) {
        this.hex = fileLines.getData();
        currentPage.setText("1");
        currentFile = fileLines;
        initComponents();
    }

    private Info createInfo(MouseEvent event) {
        int row = table.rowAtPoint(event.getPoint());
        int column = table.columnAtPoint(event.getPoint());
        return createInfo(row, column);
    }

    private Info createInfo(int row, int column) {
        if (column == 0 || model.getValueAt(row, column) == null) return null;

        String data = HexEditor.getCharFromHex((String)model.getValueAt(row, column));
        return new Info(Integer.parseInt((String)model.getValueAt(row, 0)), column - 1, data, (String) model.getValueAt(row, column));
    }

    private void showInfoInCells(int[] selectedRows, int[] selectedColumns) {
        List<Info> list = new ArrayList<>();
        for (int row : selectedRows) {
            for (int column : selectedColumns) {
                if (column == 0) continue;
                list.add(createInfo(row, column));
            }
        }
        loadInfo(list);
    }

    private void initComponents() {
        unselectCell();
        buttons.setVisible(true);
        fileName.setBorder(new EmptyBorder(10, 0, 10, 0));
        fileName.setText(title);
        Vector<String> columnNames = new Vector<>();
        columnNames.add(String.valueOf(' '));
        for (int i = 0; i < hex.get(0).size(); i++) columnNames.add(String.valueOf(i));
        model.setColumnIdentifiers(columnNames);
        for (List<String> lines : hex) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(currentFile.getNumberOfFirstRow() + model.getRowCount()));
            row.addAll(lines);
            model.addRow(row);
        }

        table.setModel(model);
        table.setForeground(styleSheet.getMainTextColor());
        table.setBackground(styleSheet.getBackBaseColor());
        table.setBorder(BorderFactory.createBevelBorder(0));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(styleSheet.getMainTextColor());
        table.setAutoscrolls(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setRowHeight(40);

        table.setRowSelectionInterval(0, 0);
        table.setColumnSelectionInterval(1, 1);

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMinWidth(50);
            }
            else break;
        }
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());
        pane.setHorizontalScrollBar(new JScrollBar(Adjustable.HORIZONTAL));

        this.tooltip.setBackground(styleSheet.getToolTipBackColor());
        this.tooltip.setForeground(styleSheet.getToolTipTextColor());
        this.tooltip.setBorder(new EmptyBorder(5, 5, 5, 5));
        pane.setViewportView(table);
        SwingUtilities.updateComponentTreeUI(this);
        pane.setVisible(true);
    }

    public void unselectCell() {
        infoPanel.start();
        positions.removeAll();
        SwingUtilities.updateComponentTreeUI(this);
        lengthOfPosition = 0;
        System.out.println("View: Cell unselected");
    }

    public void searchByMask(String mask) {
        HexEditor.findByMask(positions, mask, getHex());
        lengthOfPosition = 1;
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void searchByHex(List<String> searchingHex){
        HexEditor.find(positions, searchingHex, getHex());
        if (lengthOfPosition == 0) lengthOfPosition = searchingHex.size();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void nextPosition() {
        if (lengthOfPosition > 1) positions.getNext(lengthOfPosition);
        else positions.getNext();
        SwingUtilities.updateComponentTreeUI(this);
    }


    public void previousPosition() {
        if (lengthOfPosition > 1) positions.getPrevious(lengthOfPosition);
        else positions.getPrevious();
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void loadNextFile() {
        if (currentFile.isModified()) FileWriter.writeCacheFile(currentFile);
        if (currentFile.getNumberOfFirstRow() != FileViewer.maxRowNumberStarts()) {
            FileViewer.nextFile();
            clearModel();
            setHex(FileViewer.getCurrentCacheFile());
        }
        currentPage.setText(currentFile.getIndex() + 1 + "");
    }

    private void loadPreviousFile() {
        if (currentFile.isModified()) FileWriter.writeCacheFile(currentFile);
        if (currentFile.getNumberOfFirstRow() != 0) {
            FileViewer.previousFile();
            clearModel();
            setHex(FileViewer.getCurrentCacheFile());
        }
        currentPage.setText(currentFile.getIndex() + 1 + "");
    }

    private boolean validateData(String data) {
        return data != null && (data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$") || data.isEmpty());
    }

    private boolean validateDataArray(String[] dataArray) {
        if (dataArray == null) {
            System.out.println("Data array is null");
            return false;
        }

        boolean valid = true;
        for (String data : dataArray) {
            valid &=  validateData(data);
        }
        
        return valid;
    }

    private void loadInfo(DefaultTableModel model, int row, int column, Info info) {
        if (column == 0) return;
        if (model.getValueAt(row, column) != null) {
            infoPanel.setInfo(info);
        }
        SwingUtilities.updateComponentTreeUI(infoPanel);
    }

    private void loadInfo(List<Info> infos) {
        infoPanel.setInfo(infos);
    }

    private void showPopup(MouseEvent event, Info info) {
        lastX = event.getXOnScreen();
        lastY = event.getYOnScreen();

        Timer hoverTimer = new Timer(100, e -> {
            tooltip.setTipText(info.getInfo());
            if (popup != null) {
                popup.hide();
            }
            popup = popupFactory.getPopup(table, tooltip, lastX + 10, lastY - 10);

            lastX = event.getXOnScreen();
            lastY = event.getYOnScreen();

            scopeTimer = new Timer(1000, x -> {
                if (lastX == event.getXOnScreen() && lastY == event.getYOnScreen()) {
                    popup.show();
                }
            });
            scopeTimer.setRepeats(false);
            scopeTimer.start();

        });
        hoverTimer.setRepeats(false); 
        hoverTimer.start();
    }

    private void deleteFromModel(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean isShifted) {
        if (isShifted) {
            for (int row : selectedRows) {
                for (int column : selectedColumns) {
                    for (int i = column; i < model.getColumnCount() - 1; i++) {
                        if (i > 0) {
                            model.setValueAt(model.getValueAt(row, i + 1), row, i);
                        }
                    }
                    model.setValueAt(null, row, model.getColumnCount() - 1);
                }
            }
        } else {
            for (int row : selectedRows) {
                for (int col : selectedColumns) {
                    if (col > 0) {
                        model.setValueAt(null, row, col);
                    }
                }
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
        isModified = true;
        currentFile.updateData(getHex());
    }

    private void insertToModel(DefaultTableModel model, String[] values, int[] selectedRows, int[] selectedColumns, int valueIndex, boolean isShifted) {
        if (isShifted) {
            for (int row : selectedRows) {
                for (int column : selectedColumns) {
                    if (column > 0) {
                        for (int i = model.getColumnCount() - 1; i > column; i--) {
                            model.setValueAt(model.getValueAt(row, i - 1), row, i);
                        }
                        model.setValueAt(values[valueIndex++], row, column);
                    }
                }
            }
        }
        else {
            for (int row : selectedRows) {
                for (int column : selectedColumns) {
                    if (column > 0) model.setValueAt(values[valueIndex++], row, column);
                }
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
        isModified = true;
        currentFile.updateData(getHex());
    }

    private void copyToClipBoard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns) {
        StringBuilder sb = new StringBuilder();
        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                if (col == 0) continue;
                String data = model.getValueAt(row, col) != null ? model.getValueAt(row, col).toString() : "";
                sb.append(data);
                sb.append(";");
            }
            sb.append("\n");
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.println("Copied to clipboard");
    }

    private void updateModel(int row, int column, String newValue) {
        if (model != null && row >= 0 && column > 0 && row < model.getRowCount() && column < model.getColumnCount()) {
            model.setValueAt(newValue, row, column);
        
            List<List<String>> copyHEX = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                List<String> list = new ArrayList<>();
                for (int k = 1; k < model.getColumnCount(); k++) {
                    list.add((String)model.getValueAt(i, k));
                }
                copyHEX.add(list);
            }
            
            hex = copyHEX;
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    private void cutToClipboard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean is_shifted) {
        copyToClipBoard(model, selectedRows, selectedColumns);
        deleteFromModel(model, selectedRows, selectedColumns, is_shifted);    
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void removeFile() {
        hex = null;
        clearModel();
        FileViewer.removeCache();
    }

    private void clearModel() {
        if (model.getRowCount() != 0) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }
}
