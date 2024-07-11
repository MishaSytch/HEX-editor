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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import hex.editor.controller.HexEditor;
import hex.editor.model.CacheLines;
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
    private int lengthOfPosition = 0;
    private boolean isModified = false;
    private final JPanel buttons = new JPanel();
    private final JLabel currentPage = new JLabel();
    private CacheLines cacheLines;
    private long currentFirstRow = 0;
    private JPopupMenu popupMenu;

    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth()*0.8));
        this.infoPanel = infoPanel;
        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.setLayout(new BorderLayout());
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getBackBaseColor());

        popupMenu = createPopupMenu();    

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
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());

                    if (!table.isRowSelected(row)) {
                        table.changeSelection(row, column, false, false);
                    }
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }

                int[] selectedRow = table.getSelectedRows();
                int[] selectedColumn = table.getSelectedColumns();
                for (int row : selectedRow) {
                    for (int column : selectedColumn) {
                        if (model.getValueAt(row, column) == null) {
                            infoPanel.removeInfo();
                            return;
                        }
                    }
                }
                showInfoInCells(selectedRow, selectedColumn);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    editCell();
                }
                if (table.getSelectedColumn() > 0) {
                    int row = table.rowAtPoint(event.getPoint());
                    int column = table.columnAtPoint(event.getPoint());
                    if (column == 0 || model.getValueAt(row, column) == null) {
                        return;
                    }
                    Info info = createInfo(event);
                    loadInfo(model, row, column, info);
                }
            }
        });

        table.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent event) {
                int row = table.rowAtPoint(event.getPoint());
                int column = table.columnAtPoint(event.getPoint());

                showPopup(event, createInfo(event), model.getValueAt(row, column), column);
            }
        });

        table.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_C) {
                    copyCell();
                }

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_X) {
                    cutCell();
                }

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_V) {
                    insertCell();
                }

                if (arg0.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteCell();
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
                try {
                    loadNextCacheLines();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        previousPage.setBackground(getBackground());
        previousPage.setForeground(getStyleSheet().getMainTextColor());
        previousPage.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    loadPreviousCacheLines();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.add(buttons, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    public List<List<String>> getHex() {
        if (isModified) {
            hex.clear();
            for (Object line : model.getDataVector()) {
                List<String> row = new LinkedList<>();
                for (int i = 1; i < ((Vector<String>)line).size(); i++) {
                    row.add(((Vector<String>)line).get(i));
                }
                hex.add(row);
            }
            return hex;
        }
        return hex;
    }

    public void setHex(CacheLines cacheLines) {
        this.cacheLines = cacheLines;
        this.hex = cacheLines.getData();
        currentPage.setText("1");
        initComponents();
    }

    private void deleteCell() {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();
        if (selectedRows.length > 0 && selectedColumns.length > 0) {
            int result = JOptionPane.showConfirmDialog(null, "Do you want to delete selected cells with shift", "Delete", JOptionPane.YES_NO_OPTION);
            deleteFromModel(model, selectedRows, selectedColumns, result == JOptionPane.YES_OPTION);
        }
    }

    private void insertCell() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            List<List<String>> values = new ArrayList<>();
            for (String line : data.split("\n")) {
                List<String> inner = new ArrayList<>();
                for (String item : line.split("["+FileWriter.getSeparator()+FileWriter.getRegexForSplit().substring(1))) {
                    inner.add(item);
                }
                values.add(inner);
            }

            if (validateData(data) || validateDataArray(values)) {
                int[] selectedRows = table.getSelectedRows();
                int[] selectedColumns = table.getSelectedColumns();

                int result = JOptionPane.showConfirmDialog(null, "Do you want to insert with shift", "Insert", JOptionPane.YES_NO_OPTION);
                insertToModel(model, values, selectedRows, selectedColumns, result == JOptionPane.YES_OPTION);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Failed to paste data: " + e.getMessage());
        }
    }

    private void cutCell() {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();
        int result = JOptionPane.showConfirmDialog(null, "Do you want to cut with shift", "Cut", JOptionPane.YES_NO_OPTION);
        cutToClipboard(model, selectedRows, selectedColumns, result == JOptionPane.YES_OPTION);
    }

    private void copyCell() {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();
        copyToClipBoard(model, selectedRows, selectedColumns);
    }

    private void editCell() {
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        if (column != 0) {
            String text = JOptionPane.showInputDialog(null, "Edit cell:").trim();
            if (validateData(text) || text.isEmpty()) {
                model.setValueAt(text.toLowerCase(), row, column);
                isModified = true;
                cacheLines.updateData(getHex());
            } else {
                JOptionPane.showConfirmDialog(null, "Not valid!");
            }
        }
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
        infoPanel.start();
        unselectCell();
        buttons.setVisible(true);
        fileName.setBorder(new EmptyBorder(10, 0, 10, 0));
        fileName.setText(title);
        Vector<String> columnNames = new Vector<>();
        columnNames.add(String.valueOf(' '));
        for (int i = 0; i < hex.get(0).size(); i++) columnNames.add(String.valueOf(i));
        model.setColumnIdentifiers(columnNames);
        for (int i = 0; i < hex.size(); i++) {
            List<String> lines = hex.get(i);
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(currentFirstRow + i));
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
        positions.removeAll();
        SwingUtilities.updateComponentTreeUI(this);
        lengthOfPosition = 0;
    }

    public void searchByMask(String mask) {
        HexEditor.findByMask(positions, mask, getHex());
        lengthOfPosition = 1;
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void searchByHex(List<String> searchingHex){
        HexEditor.find(positions, searchingHex, getHex());
        lengthOfPosition = searchingHex.size();
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void removeFile() {
        hex = null;
        clearModel();
        FileViewer.delete();
    }

    private void loadNextCacheLines() throws IOException {
        if (cacheLines.isModified())FileWriter.writeInCacheFile(cacheLines);
        if (!FileViewer.isLast()) {
            currentFirstRow += model.getRowCount();
            clearModel();
            setHex(FileViewer.getNextLines());
            currentPage.setText(cacheLines.getPart() + "");
        }
    }
    
    private void loadPreviousCacheLines() throws IOException {
        if (cacheLines.isModified()) FileWriter.writeInCacheFile(cacheLines);
        if (cacheLines.getPart() != 1) {
            currentFirstRow -= model.getRowCount();
            clearModel();
            setHex(FileViewer.getPreviousLines());
            currentPage.setText(cacheLines.getPart() + "");
        }
    }

    private boolean validateData(String data) {
        return data != null && (data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$") || data.isEmpty());
    }

    private boolean validateDataArray(List<List<String>> dataArray) {
        if (dataArray == null || dataArray.isEmpty()) {
            return false;
        }

        boolean valid = true;
        for (List<String> line : dataArray) {
            for (String data : line) {
                valid &= validateData(data);
            }
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

    private void showPopup(MouseEvent event, Info info, Object value, int column) {
        lastX = event.getXOnScreen();
        lastY = event.getYOnScreen();

        if (info == null || info.getInfo() == null || popupMenu.isVisible()) return;

        Timer hoverTimer = new Timer(100, e -> {
            tooltip.setTipText(info.getInfo());
            if (popup != null && value == null || popup != null || column == 0) {
                popup.hide();
            }
            popup = popupFactory.getPopup(table, tooltip, lastX + 10, lastY - 10);

            lastX = event.getXOnScreen();
            lastY = event.getYOnScreen();

            scopeTimer = new Timer(1000, x -> {
                if (lastX == event.getXOnScreen() && lastY == event.getYOnScreen() &&  !popupMenu.isVisible()) {
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
        for (int row : selectedRows) {
            if (isShifted) {
                for (int column = Math.max(1, selectedColumns[0]); column < model.getColumnCount(); column++) {
                    if (column + selectedColumns.length < model.getColumnCount()) {
                        model.setValueAt(model.getValueAt(row, column + selectedColumns.length), row, column);    
                    } else {
                        model.setValueAt(null, row, column);    
                    }
                }
            } else {
                for (int column : selectedColumns) {
                    model.setValueAt(null, row, column);
                }
            }  
        }
        SwingUtilities.updateComponentTreeUI(this);
        isModified = true;
        cacheLines.updateData(getHex());
    }

    private void insertToModel(DefaultTableModel model, List<List<String>> values, int[] selectedRows, int[] selectedColumns, boolean isShifted) {
        int valueRow = 0;
        int valueColumn = 0;
        for (int row : selectedRows) {
            for (int column : selectedColumns) {
                if (column > 0) {
                    if (isShifted) for (int i = model.getColumnCount() - 1; i > column; i--) {
                        model.setValueAt(model.getValueAt(row, i - 1), row, i);
                    }
                    model.setValueAt(
                        valueRow >= values.size() ? null 
                                                    : valueColumn >= values.get(valueRow).size() ? null 
                                                                                                : values.get(valueRow).get(valueColumn++), row, column
                    );
                    if (valueRow < values.size() && valueColumn >= values.get(valueRow).size()) {
                        valueColumn = 0;
                        valueRow++;
                    }
                }
            }
        }
            
        SwingUtilities.updateComponentTreeUI(this);
        isModified = true;
        cacheLines.updateData(getHex());
    }

    private void copyToClipBoard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns) {
        StringBuilder sb = new StringBuilder();
        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                if (col == 0) continue;
                String data = model.getValueAt(row, col) != null ? model.getValueAt(row, col).toString() : "";
                sb.append(data);
                sb.append(FileWriter.getSeparator());
            }
            sb.append("\n");
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void cutToClipboard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean is_shifted) {
        copyToClipBoard(model, selectedRows, selectedColumns);
        deleteFromModel(model, selectedRows, selectedColumns, is_shifted);    
    }

    private void clearModel() {
        if (model.getRowCount() != 0) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem edit = new JMenuItem("edit");
        edit.addActionListener(e -> {
            editCell();
        });

        JMenuItem cut = new JMenuItem("cut");
        cut.addActionListener(e -> {
            cutCell();
        });

        JMenuItem delete = new JMenuItem("delete");
        delete.addActionListener(e -> {
            deleteCell();
        });

        JMenuItem copy = new JMenuItem("copy");
        copy.addActionListener(e -> {
            copyCell();
        });

        JMenuItem insert = new JMenuItem("insert");
        insert.addActionListener(e -> {
            insertCell();
        });

        popupMenu.add(edit);
        popupMenu.add(copy);
        popupMenu.add(insert);
        popupMenu.add(delete);
        popupMenu.add(cut);

        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e) {
                if (table.getSelectedRowCount() > 1 || table.getSelectedColumnCount() > 1) {
                    edit.setEnabled(false);
                } else {
                    edit.setEnabled(true);
                }
            }
        });;

        return popupMenu;
    }
}
