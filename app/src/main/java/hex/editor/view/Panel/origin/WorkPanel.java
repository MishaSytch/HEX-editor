package hex.editor.view.Panel.origin;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Exchanger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import hex.editor.model.Info;
import hex.editor.model.Types;
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
    private final Exchanger<Object> hexExchanger;
    private final Exchanger<Object> charsExchanger;
    private final Exchanger<Object> integerExchanger;
    private final Exchanger<Object> UPDATE_BY_HEXExchanger;
    private final JScrollPane pane = new JScrollPane();
    private final JLabel fileName = getText("");
    private List<List<String>> hex;
    private final InfoPanel infoPanel;
    private List<List<Integer>> positions;
    private final DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };
    private final JToolTip tooltip = new JToolTip();
    private Integer lastX;
    private Integer lastY;
    private final PopupFactory popupFactory = PopupFactory.getSharedInstance();
    private Popup popup;
    private Timer scopeTimer;
    private String title;

    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth()*0.8));
        this.infoPanel = infoPanel;
        this.hexExchanger = exchangers.get(Types.HEX);
        this.charsExchanger = exchangers.get(Types.CHARS);
        this.integerExchanger = exchangers.get(Types.INTEGER);
        this.UPDATE_BY_HEXExchanger = exchangers.get(Types.UPDATE_BY_HEX);

        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.setLayout(new BorderLayout());
        this.setBackground(styleSheet.getBackBaseColor());
        this.setForeground(styleSheet.getBackBaseColor());

        pane.setVisible(false);
        this.add(pane, BorderLayout.CENTER);
        this.add(fileName, BorderLayout.NORTH);
    }

    public List<List<String>> getHex() {
        return hex;
    }
    public void setHex(List<List<String>> hex) {
        this.hex = hex;
        initComponents();
    }

    private void initComponents() {
        fileName.setBorder(new EmptyBorder(10, 0, 10, 0));
        fileName.setText(title);

        Vector<String> columnNames = new Vector<>();
        columnNames.add(String.valueOf(' '));
        for (int i = 0; i < hex.get(0).size(); i++) columnNames.add(String.valueOf(i));
        model.setColumnIdentifiers(columnNames);
        for (List<String> lines : hex) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(model.getRowCount()));
            row.addAll(lines);
            model.addRow(row);
        }

        table.setModel(model);
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
            public void mouseReleased(MouseEvent event) {
            }
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
            public void editingStopped(ChangeEvent event) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                String newValue = (String) table.getValueAt(row, column);
                updateModel(row, column, newValue);
            }

            @Override
            public void editingCanceled(ChangeEvent event) {
            }
        });

        table.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {}

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
                    updateService();
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
                            updateService();
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
                        updateService();
                        System.out.println("View: deleted selected cells");
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }

        });

        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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

    @SuppressWarnings("unchecked")
    public void waitPosition() {
        if (hex == null) return;

        System.out.println("View: Position wait");
        try {
            positions = (List<List<Integer>>) integerExchanger.exchange(null);
            if (positions.isEmpty()) positions = null;
            else {
                System.out.println("View: Position got");
                selectCell(positions);
            }
        } catch (InterruptedException ignored) {
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
        } catch (InterruptedException ignored) {
        }
    }

    private void showPopup(MouseEvent event) {
        int[] selectedColumns = table.getSelectedColumns();

        lastX = event.getXOnScreen();
        lastY = event.getYOnScreen();

        JSlider infoSlider = new JSlider(JSlider.HORIZONTAL, 0, selectedColumns.length - 1, 0);
        infoSlider.setMajorTickSpacing(1);
        infoSlider.setPaintTicks(true);
        infoSlider.setPaintLabels(true);

        Timer hoverTimer = new Timer(100, e -> {
            if (selectedColumns.length > 1) {
                for (int column : selectedColumns) {
                    infoSlider.add(getText(new Info(table.getSelectedRow(), column, "", table.getValueAt(table.getSelectedRow(), column).toString()).getInfo()));
                }
                tooltip.add(infoSlider);
            } else {
                tooltip.setTipText(infoPanel.getInfo().getText());
            }
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
                    if(i_selectedRows >= selectedRows.length || i != selectedRows[i_selectedRows] || k_selectedColumn >= selectedColumns.length || k != selectedColumns[k_selectedColumn]
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
        initComponents();
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
        updateService();
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
        updateService();
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("Model updated at row: " + row + " column: " + (column - 1));
        }
    }

    private void cutToClipboard(DefaultTableModel model, int[] selectedRows, int[] selectedColumns, boolean is_shifted) {
        copyToClipBoard(model, selectedRows, selectedColumns);
        deleteFromModel(model, selectedRows, selectedColumns, is_shifted);    
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
