package hex.editor.view.Panel.origin;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import java.util.concurrent.Exchanger;

import hex.editor.services.TableViewer;
import hex.editor.view.MainWindow;
import hex.editor.view.Style.IStyleSheet;

public class WorkPanel extends BasePanel {
    private JTable table;
    private IStyleSheet styleSheet = super.getStyleSheet();
    private Exchanger<String[]> dataExchanger;
    private MainWindow mainWindow;

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
        JLabel text = super.getText(title);
        text.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.add(text, BorderLayout.NORTH);
    }

    public void showData() {        
        this.removeAll();
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
        
        TableModel box = TableViewer.getTable(data, this.getWidth());

        table = new JTable(box);
        table.setBorder(new EmptyBorder(10, 2, 2, 2));
        
        // Настройка таблицы
        table.setRowHeight(30);
        table.setRowHeight(10, 20);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowVerticalLines(true);
        table.setGridColor(styleSheet.getMainTextColor());
        table.setBackground(styleSheet.getBackBaseColor());
        table.setForeground(styleSheet.getMainTextColor());

        this.add(table, BorderLayout.CENTER);
        mainWindow.setVisible(false);
        mainWindow.setVisible(true);
        System.out.println("View: data reloaded");
    }
}
