package hex.editor.view.Panel.origin;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.util.concurrent.Exchanger;
import java.util.*;

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
    private MainWindow mainWindow;
    private JScrollPane pane;
    private JLabel text;
    private JLabel line_number;
    private String[] hex;
    private DefaultTableModel model;
    private InfoPanel infoPanel;

    public WorkPanel(MainWindow mainWindow, InfoPanel infoPanel, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.8));
        this.infoPanel = infoPanel;
        this.mainWindow = mainWindow;
        this.hexExchanger = exchangers.get(Types.HEX);
        this.charsExchanger = exchangers.get(Types.CHARS);
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

        table = new JTable(model);
        table.setBorder(BorderFactory.createBevelBorder(1));
        table.setRowHeight(30);
        table.setRowHeight(10, 20);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setCursor(new Cursor(1));
        table.setGridColor(styleSheet.getMainTextColor());
        table.setBackground(styleSheet.getBackBaseColor());
        table.setForeground(styleSheet.getMainTextColor());
        table.setAutoscrolls(true);
        table.getTableHeader().setReorderingAllowed(false);

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


        // Настройка нумерации строк
        line_number = new JLabel();
        line_number.setBackground(styleSheet.getBackBaseColor());
        line_number.setForeground(styleSheet.getBackBaseColor());
        line_number.setPreferredSize(new Dimension(40, 200));

        // Настройка панели с прокруткой
        pane = new JScrollPane(table);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());
        pane.setPreferredSize(new Dimension(table.getWidth(), table.getHeight()));

        this.add(pane);

        mainWindow.setVisible(false);
        mainWindow.setVisible(true);
        System.out.println("View: hex loaded");
    }
}
