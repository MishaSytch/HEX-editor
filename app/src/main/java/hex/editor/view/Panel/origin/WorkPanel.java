package hex.editor.view.Panel.origin;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.Exchanger;

import hex.editor.services.TableViewer;
import hex.editor.view.MainWindow;
import hex.editor.view.Style.IStyleSheet;

public class WorkPanel extends BasePanel {
    private JTable table;
    private IStyleSheet styleSheet = super.getStyleSheet();
    private Exchanger<String[]> dataExchanger;
    private MainWindow mainWindow;
    private JScrollPane pane;
    private JLabel text;

    public WorkPanel(MainWindow mainWindow, Exchanger<String[]> dataExchanger) {
        super(mainWindow.getHeight(), mainWindow.getWidth());
        this.mainWindow = mainWindow;
        this.dataExchanger = dataExchanger;
        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.setLayout(new BorderLayout());

        table = new JTable();
        table.setBorder(BorderFactory.createBevelBorder(1));
    }

    public void setTitle(String title) {
        if (text != null) this.remove(text);

        text = super.getText(title);
        text.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.add(text, BorderLayout.NORTH);
    }

    public void showData() {
        if (pane != null) {
            pane.removeAll();
            this.remove(pane);
        }
        String[] data = new String[]{""};
        try {
            System.out.println("View: wait data");
            data = dataExchanger.exchange(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("View: reciaved data");

        int colomns_count = 15;

        String[] colomns = new String[colomns_count];
        for (int i = 0 ; i < colomns_count; i++) colomns[i] = String.valueOf(i);

        DefaultTableModel model = TableViewer.getTable(data, this.getWidth());

        table = new JTable(model);
        
        // Настройка таблицы
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

        table.addFocusListener(new FocusAdapter()
        {
            public void focusLost( FocusEvent e ) {
                table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn()).stopCellEditing();
            }
        });
        
        pane = new JScrollPane(table);
        pane.setBackground(styleSheet.getBackBaseColor());
        pane.setForeground(styleSheet.getBackBaseColor());
        pane.setPreferredSize(new Dimension(table.getWidth(), table.getHeight()));
        
        System.out.println(table.HEIGHT);


        this.add(pane, BorderLayout.CENTER);

        mainWindow.setVisible(false);
        mainWindow.setVisible(true);
        System.out.println("View: data reloaded");
    }
}
