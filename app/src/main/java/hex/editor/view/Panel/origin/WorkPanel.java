package hex.editor.view.Panel.origin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.checkerframework.checker.nullness.qual.KeyFor;

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
    private String[] hex;
    private InfoPanel infoPanel;
    private Integer[] positions;
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
            while (true) {
                hex = (String[])hexExchanger.exchange(null);
                if (hex != null) break;
            }
        } catch (InterruptedException e) {}

        System.out.println("View: reciaved hex");

        // Создание модели
        int colomns_count = 15;
        String[] colomns = new String[colomns_count];
        for (int i = 0 ; i < colomns_count; i++) 
            colomns[i] = String.valueOf(i);

        model = TableViewer.getTable(hex, this.getWidth());

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
                positions = (Integer[]) integerExchanger.exchange(null);
        } catch (InterruptedException e) {
        }
        System.out.println("View: Posintion got");
        selectCell(positions);
    }

    public void selectCell(Integer[] pos) {
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
                    for (Integer pos : positions) {
                        int row_this = pos / model.getColumnCount();
                        int column_this = pos % model.getColumnCount();
                        if (row == row_this && column == column_this) {
                            c.setBackground(new Color(123, 123, 123));
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
        table.setRowHeight(50);

        table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent event) {
                System.out.println("Info: wait info");
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

                String ch = "empty";
                try {
                    if (model.getValueAt(row, column) != "") {
                        hexExchanger.exchange(new String[]{ (String)model.getValueAt(row, column) });
                        ch = ((String[])(charsExchanger.exchange(null)))[0];
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

        table.addColumnSelectionInterval(0, 3);


        // Настройка панели с прокруткой
        pane = new JScrollPane(table);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());
        pane.setPreferredSize(new Dimension(table.getWidth(), table.getHeight()));
        this.add(pane);
    }
} 
