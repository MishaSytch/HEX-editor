package hex.editor.view.Panel.origin;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import hex.editor.view.Style.IStyleSheet;
import hex.editor.model.Info;
import hex.editor.model.Types;
import hex.editor.services.TableViewer;

import javax.swing.BorderFactory;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.Exchanger;

public class WorkPanel extends BasePanel {
    private JTable table;
    private IStyleSheet styleSheet = super.getStyleSheet();
    private Exchanger<Object> hexExchanger;
    private Exchanger<Object> charsExchanger;
    private Exchanger<Object> integerExchanger;
    private Exchanger<Object> UPDATE_BY_HEXExchanger;
    private JScrollPane pane;
    private JLabel text;
    private List<List<String>> hex;
    private InfoPanel infoPanel;
    private List<List<Integer>> positions;
    private DefaultTableModel model;
    private JToolTip tooltip = new JToolTip();
    private Integer lastX;
    private Integer lastY;
    private PopupFactory popupFactory = PopupFactory.getSharedInstance();
    private Popup popup;
    private Timer hoverTimer;
    private Timer scopeTimer;


    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.8));
        this.infoPanel = infoPanel;
        this.hexExchanger = exchangers.get(Types.HEX);
        this.charsExchanger = exchangers.get(Types.CHARS);
        this.integerExchanger = exchangers.get(Types.INTEGER);
        this.UPDATE_BY_HEXExchanger = exchangers.get(Types.UPDATE_BY_HEX);

        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.setLayout(new BorderLayout());
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getBackBaseColor());

        this.tooltip.setBackground(styleSheet.getToolTipBackColor());
        this.tooltip.setForeground(styleSheet.getToolTipTextColor());
        this.tooltip.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    public void setTitle(String title) {
        if (text != null) this.remove(text);

        text = super.getText(title);
        text.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.add(text, BorderLayout.NORTH);
    }

    @SuppressWarnings("unchecked")
    public void showData() {
        System.out.println("View: wait hex");
        try {
            if (pane != null) {
                pane.removeAll();
                this.remove(pane);
            }

            hex = (List<List<String>>) hexExchanger.exchange(null);
            System.out.println("View: received hex");
            model = TableViewer.getTable(hex);

            createAndAddTable(model);
            SwingUtilities.updateComponentTreeUI(this);
            System.out.println("View: hex loaded");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("View: hex loading interrupted");
        }
    }

    @SuppressWarnings("unchecked")
    public void waitPosition() {
        if (hex == null) return;

        System.out.println("View: Position wait");
        try {
                positions = (List<List<Integer>>) integerExchanger.exchange(null);
                if (positions.size() == 0) positions = null;
                else {
                    System.out.println("View: Position got");
                    selectCell(positions);
                }
        } catch (InterruptedException e) {
        }
    }

    public void selectCell(List<List<Integer>> pos) {
        positions = pos;
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("View: Cell selected");
    }

    public void unselectCell() {
        positions = null;    
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("View: Cell unselected");
    }

    public void updateData() {
        if (hex == null) return;

        updateService();
    }

    private void createAndAddTable(DefaultTableModel model) {
        if (table != null) {
            table.removeAll();
            pane.remove(table);
            this.remove(pane);
        }
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
                c.setBackground(styleSheet.getBackBaseColor());
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                c.setEnabled(columnIndex > 0);
                if (positions != null) {
                    for (List<Integer> rows : positions) {
                        if (rowIndex == positions.indexOf(rows)) {
                            for (Integer column : rows) {
                                if (columnIndex == column + 1) {
                                    c.setBackground(styleSheet.getSelectedColor());
                                        
                                    return c;
                                } 
                           }  
                        }            
                    }
                }
                if (isSelected) {
                    c.setBackground(styleSheet.getSelectedColor());
                }

                return c;
            }
        });
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


        table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (table.getSelectedColumn() > 0) {
                    System.out.println("Info: wait info");
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
    
                    loadInfo(model, row, column);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {}

            @Override
            public void mouseExited(MouseEvent event) {}

            @Override
            public void mousePressed(MouseEvent event) {}

            @Override
            public void mouseReleased(MouseEvent event) {}
        });

        table.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
                int row = table.rowAtPoint(event.getPoint());
                int column = table.columnAtPoint(event.getPoint());
                loadInfo(model, row, column);
                showPopup(event);                               
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
            }
        });

        table.getDefaultEditor(String.class).addCellEditorListener( new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                String newValue = (String) table.getValueAt(row, column);
                updateModel(row, column, newValue);
            }
        
            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        table.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                
            }

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

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
            
        });

        // Настройка панели с прокруткой
        pane = new JScrollPane(table);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());

        this.add(pane);
    }

    private boolean validateData(String data) {
        return data != null && (data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$") || data.length() == 0);
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

    @SuppressWarnings("unchecked")
    private void loadInfo(DefaultTableModel model, int row, int column) {
        if (column == 0) return;
        String ch = "empty";
        try {
            if (model.getValueAt(row, column) != "") {
                List<List<String>> cells = new ArrayList<>();
                List<String> cell = new ArrayList<>();
                cell.add((String)model.getValueAt(row, column));
                cells.add(cell);
                hexExchanger.exchange(cells);
                ch = ((List<List<String>>)(charsExchanger.exchange(null))).get(0).get(0);
            }

            infoPanel.setInfo(new Info(row, column - 1, ch, (String)model.getValueAt(row, column)));
            SwingUtilities.updateComponentTreeUI(infoPanel);
        } catch (InterruptedException e) {
        }
    }

    private void loadInfo(DefaultTableModel model, int[] selectedRows, int[] selectedColumns) {
        List<List<Info>> blockValues = new ArrayList<>();
        for (int row : selectedRows) {
            List<Info> rowData = new ArrayList<>();
            for (int column : selectedColumns) {
                String ch = "empty";
                try {
                    if (model.getValueAt(row, column) != "") {
                    List<List<String>> cells = new ArrayList<>();
                    List<String> cell = new ArrayList<>();
                    cell.add((String)model.getValueAt(row, column));
                    cells.add(cell);
                    hexExchanger.exchange(cells);
                    ch = ((List<List<String>>)(charsExchanger.exchange(null))).get(0).get(0);
                    }
                    SwingUtilities.updateComponentTreeUI(infoPanel);
                } catch (InterruptedException e) {
                }
                rowData.add(new Info(row, column - 1, ch, (String)model.getValueAt(row, column)));
                blockValues.add(rowData);       
            }
        }
        SwingUtilities.updateComponentTreeUI(infoPanel);
    }

    private void showPopup(MouseEvent event) {
        lastX = event.getXOnScreen();
        lastY = event.getYOnScreen();

        hoverTimer = new Timer(100, e -> {
            tooltip.setTipText(infoPanel.getInfo().getText());
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

    private void deleteFromModel(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean is_shifted) {
        int i_selectedRows = 0;
        List<List<String>> copyHEX = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            int k_selectedColumn = selectedColumns[0] == 0 ? 1 : 0;
            List<String> list = new ArrayList<>();
            for (int k = 1; k < model.getColumnCount(); k++) {
                if (is_shifted) {
                    if(i_selectedRows >= selectedRows.length || i_selectedRows < selectedRows.length && i != selectedRows[i_selectedRows] 
                    || k_selectedColumn >= selectedColumns.length || k_selectedColumn < selectedColumns.length && k != selectedColumns[k_selectedColumn]
                    ) {
                        list.add((String)model.getValueAt(i, k));
                    } else {
                        k_selectedColumn++;
                    }
                } else {
                    if (k == selectedColumns[k_selectedColumn] && i == selectedRows[i_selectedRows]) {
                        k_selectedColumn++;
                        list.add("");
                    } else {
                        list.add((String)model.getValueAt(i, k));
                    }
                }
            }
            copyHEX.add(list);
            i_selectedRows++;
        }
        
        hex = copyHEX;
        model = TableViewer.getTable(hex);
        createAndAddTable(model);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void insertToModel(DefaultTableModel model, String[] values, int[] selectedRows, int[] selectedColumns,int valueIndex, boolean is_shifted) {
        List<List<String>> copyHEX = new ArrayList<>();
        int i_selectedRows = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            int k_selectedColumn = selectedColumns[0] == 0 ? 1 : 0;
            boolean visited = false;
            List<String> list = new ArrayList<>();
            Deque<String> queue = new ArrayDeque<>();
            for (int k = 1; k < model.getColumnCount(); k++) {
                if (
                    k_selectedColumn < selectedColumns.length && k == selectedColumns[k_selectedColumn]
                    && i_selectedRows < selectedRows.length && i == selectedRows[i_selectedRows]
                ) {
                    k_selectedColumn++;
                    visited = true;

                    if (is_shifted) queue.addLast((String)model.getValueAt(i, k));
                    list.add(
                        valueIndex < values.length 
                        ? values[valueIndex++] 
                        : is_shifted 
                            ? "" 
                            : (String)model.getValueAt(i, k)
                    );
                } else {
                    if (!queue.isEmpty()) {
                        list.add(queue.removeFirst());
                        k--;
                    }
                    else {
                        list.add((String)model.getValueAt(i, k));
                    }

                }
            }
            if (visited) i_selectedRows++;
            copyHEX.add(list);
        }
        
        hex = copyHEX;
        model = TableViewer.getTable(hex);
        createAndAddTable(model);
        SwingUtilities.updateComponentTreeUI(this);
    }


    private void copyToClipBoard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns) {
        StringBuilder sb = new StringBuilder();
        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                if (col == 0) continue;

                sb.append(model.getValueAt(row, col).toString());
                sb.append(";");
            }
            sb.append("\n");
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.println("Copied to clipboard");
    }

    private void updateService() {
        try {
            System.out.println("View: Update by hex");
            UPDATE_BY_HEXExchanger.exchange(hex);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
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
        model = TableViewer.getTable(hex);
        createAndAddTable(model);
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("Model updated at row: " + row + " column: " + (column - 1));
        }
    }

    private void cutToClipboard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean is_shifted) {
        copyToClipBoard(model, selectedRows, selectedColumns);
        deleteFromModel(model, selectedRows, selectedColumns, is_shifted);    
    }

    
} 
