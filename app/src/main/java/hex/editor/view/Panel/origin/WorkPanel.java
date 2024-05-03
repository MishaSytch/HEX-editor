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
import java.util.concurrent.Exchanger;

public class WorkPanel extends BasePanel {
    private JTable table;
    private IStyleSheet styleSheet = super.getStyleSheet();
    private Exchanger<Object> hexExchanger;
    private Exchanger<Object> charsExchanger;
    private Exchanger<Object> integerExchanger;
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
            update();
            System.out.println("View: hex loaded");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("View: hex loading interrupted");
        }
    }

    private void update() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void waitPosition() {
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
        this.remove(pane);
        pane.remove(table);
        createAndAddTable(model);
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("View: Cell selected");
    }

    public void unselectCell() {
        positions = null;    
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("View: Cell unselected");
    }

    private void createAndAddTable(DefaultTableModel model) {
        if (table != null) {
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
                                if (columnIndex == column) {
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
    
                    loadInfo(model, row, column - 1);
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
                loadInfo(model, row, column - 1);
                showPopup(event);                               
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
            }
        });

        table.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_C) {
                    copyToClipBoard(model);
                }
                    
                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_V) {
                    try {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        String data = (String) clipboard.getData(DataFlavor.stringFlavor);
                        String[] values = data.split("\n|\t");
                        if (validateData(data) || validateDataArray(values)) {
                            int[] selectedRows = table.getSelectedRows();
                            int[] selectedColumns = table.getSelectedColumns();
                            int valueIndex = 0;

                            int result = JOptionPane.showConfirmDialog(null, "Do you want to insert with shift", "Insert", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION) {
                                insertWithShiftToModel(model, values, selectedRows, selectedColumns, valueIndex);
                            } else {
                                insertToModel(model, values, selectedRows, selectedColumns, valueIndex);
                            }
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
                    
                    deleteFromModel(model, selectedRows, selectedColumns);
                    update();
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
        // Проверка, что строка не пуста и соответствует определенному формату:
        return data != null && data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$|[\\s]*");
    }

    private boolean validateDataArray(String[] dataArray) {
        if (dataArray == null) {
            System.out.println("Data array is null");
            return false;
        }
        
        for (String data : dataArray) {
            if (data.trim().isEmpty()) {
                return true;
            }
            if (!data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$|[\\s]*$")) {
                System.out.println("Invalid data: contains non-alphanumeric characters");
                return false;
            }
        }
        
        return true;
    }

    private void loadInfo(DefaultTableModel model, int row, int column) {
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

            infoPanel.setInfo(new Info(row, column, ch, (String)model.getValueAt(row, column)));
            SwingUtilities.updateComponentTreeUI(infoPanel);
        } catch (InterruptedException e) {
        }
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

    private void deleteFromModel(DefaultTableModel model, int[] selectedRows, int[] selectedColumns) {
        if (selectedRows.length > 0 && selectedColumns.length > 0) {
            int result = JOptionPane.showConfirmDialog(null, "Do you want to delete selected cells with shift", "Delete", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                for (int row : selectedRows) {
                    for (int col : selectedColumns) {
                        if (col == 0) continue;
                        for (int shiftCol = col; shiftCol < table.getColumnCount() - 1; shiftCol++) {
                            table.setValueAt(table.getValueAt(row, shiftCol + 1), row, shiftCol);
                        }
                        table.setValueAt("", row, table.getColumnCount() - 1);
                    }
                }
            }
            else {
                for (int row : selectedRows) {
                    for (int col : selectedColumns) {
                        if (col == 0) continue;
                        model.setValueAt("", row, col);
                    }
                }
            }
        }
    }

    private void insertWithShiftToModel(DefaultTableModel model, String[] values, int[] selectedRows, int[] selectedColumns,int valueIndex) {
        List<List<String>> copyHEX = new ArrayList<>();
        for (List<String> line : hex) {
            List<String> list = new ArrayList<>();
            for (String cell : line) {
                list.add(cell);
            }
            copyHEX.add(list);
        }

        for (int row : selectedRows) {
            Deque<String> queue = new ArrayDeque<>();
            List<String> tmpHex = copyHEX.get(row);

            int i_column = 0;
            for (int i = selectedColumns[i_column]; true; i++) {
                if (i == 0) continue;
                
                while (i > tmpHex.size() - 1) {
                    tmpHex.add("");
                }
                if (i_column < selectedColumns.length && i == selectedColumns[i_column++]) {
                    queue.addLast(tmpHex.get(i));
                    tmpHex.set(i, valueIndex < values.length ? values[valueIndex++] : "");
                } else {
                    tmpHex.set(i, queue.removeFirst());
                    if (queue.isEmpty() && i_column == selectedColumns.length) {
                        break;
                    }
                }
            }
            copyHEX.set(row, tmpHex);
        }
        hex = copyHEX;
        model = TableViewer.getTable(hex);
        createAndAddTable(model);
        update();
    }

    private void insertToModel(DefaultTableModel model, String[] values, int[] selectedRows, int[] selectedColumns,int valueIndex) {
        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                if (col == 0) continue;
                if (values.length < selectedColumns.length * selectedRows.length) {
                    String[] tmpValues = new String[selectedColumns.length * selectedRows.length];
                    for(int i = 0; i < tmpValues.length; i++) {
                        if (i < values.length) {
                            tmpValues[i] = values[i];
                        } else {
                            tmpValues[i] = "";
                        }
                    }
                    values = tmpValues;
                }
                if (valueIndex < values.length) {
                    model.setValueAt(values[valueIndex++], row, col);
                }
            }
        }
        update();
    }

    private void copyToClipBoard(DefaultTableModel model) {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();
        StringBuilder sb = new StringBuilder();
        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                sb.append(model.getValueAt(row, col).toString());
                sb.append("\t");
            }
            sb.append("\n");
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.println("Copied to clipboard");
    }
} 
