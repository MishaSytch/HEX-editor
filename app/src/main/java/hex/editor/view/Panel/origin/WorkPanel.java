package hex.editor.view.Panel.origin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import hex.editor.model.Info;
import hex.editor.model.Types;
import hex.editor.services.TableViewer;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import hex.editor.view.Style.IStyleSheet;

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
        } catch (InterruptedException e) {
        }
        System.out.println("View: Position got");
        selectCell(positions);
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
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table1, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
                Component c = super.getTableCellRendererComponent(table1, value, isSelected, hasFocus, rowIndex, columnIndex);

                c.setBackground(styleSheet.getBackBaseColor());
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);

                // Disable editing for the first column
                c.setEnabled(columnIndex > 0);

                if (positions != null) {
                    for (List<Integer> columns : positions) {
                        if (columns.contains(columnIndex)) {
                            c.setBackground(styleSheet.getSelectedColor());
                            break;
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
                System.out.println("Info: wait info");
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

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

            @Override
            public void mouseEntered(MouseEvent event) {
                
                
            }

            @Override
            public void mouseExited(MouseEvent event) {
                
            }

            @Override
            public void mousePressed(MouseEvent event) {
                
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                
            }
        });

        table.addKeyListener(new KeyListener() {

            private boolean isControlDown;

            @Override
            public void keyPressed(KeyEvent arg0) {
                
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.isControlDown()) {
                    isControlDown = true;
                } else {
                    isControlDown = false;
                }

                if (arg0.getKeyCode() == KeyEvent.VK_S && isControlDown) {
                    infoPanel.showSearchWindow();
                    update();
                }

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_C) {
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

                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_V) {
                    try {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        String data = (String) clipboard.getData(DataFlavor.stringFlavor);
                        if (validateData(data)) {
                            String[] values = data.split("[\t\n]+");
                            int[] selectedRows = table.getSelectedRows();
                            int[] selectedColumns = table.getSelectedColumns();
                            int valueIndex = 0;
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
                        if (result == JOptionPane.YES_OPTION) {
                            for (int row : selectedRows) {
                                for (int col : selectedColumns) {
                                    if (col == 0) continue;
                                    // Смещаем значения вправо от удаленной ячейки влево
                                    for (int shiftCol = col; shiftCol < table.getColumnCount() - 1; shiftCol++) {
                                        table.setValueAt(table.getValueAt(row, shiftCol + 1), row, shiftCol);
                                    }
                                    // Очищаем значение в последней ячейке строки
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
        return data != null && (validateDataArray(data.split("[\t\n]+")) || data.matches("^[a-fA-F0-9]{2}$|^[a-fA-F0-9]{4}$|[\\s]*"));
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
} 
