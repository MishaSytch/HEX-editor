package hex.editor.view.Panel.origin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
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
    private MainWindow mainWindow;
    private JScrollPane pane;
    private JLabel text;
    private List<List<String>> hex;
    private InfoPanel infoPanel;
    private List<List<Integer>> positions;
    private DefaultTableModel model;

    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.8));
        this.infoPanel = infoPanel;
        this.mainWindow = mainWindow;
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
        // Подготовка
        if (pane != null) {
            pane.removeAll();
            this.remove(pane);
        }
        // Получение данных
        
        System.out.println("View: wait hex");
        try {
            hex = (List<List<String>>)hexExchanger.exchange(null);
        } catch (InterruptedException e) {}

        System.out.println("View: reciaved hex");

        // Создание модели
        model = TableViewer.getTable(hex);

        createAndAddTable(model);
        
        update();
        System.out.println("View: hex loaded");
    }

    private void update() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void waitPosition() {
        System.out.println("View: Posintion wait");
        try {
                positions = (List<List<Integer>>) integerExchanger.exchange(null);
        } catch (InterruptedException e) {
        }
        System.out.println("View: Posintion got");
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
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                c.setBackground(styleSheet.getBackBaseColor());
                if (positions != null) {
                    for (int row_this = 0; row_this < positions.size(); row_this++) {
                        if (row == row_this) {
                            for (Integer column_this : positions.get(row_this)) {
                                if (column == column_this) {
                                    c.setBackground(new Color(123, 123, 123));
                                }
                            }
                        }
                    }
                }
                return c;
            }
        });
        table.setForeground(styleSheet.getMainTextColor());

        table.setBorder(BorderFactory.createBevelBorder(0));
        table.setRowHeight(30);
        table.setRowHeight(10, 20);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(styleSheet.getMainTextColor());
        
        table.setAutoscrolls(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
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
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
            
        });

        // Настройка панели с прокруткой
        pane = new JScrollPane(table);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());
        this.add(pane);
    }
} 
