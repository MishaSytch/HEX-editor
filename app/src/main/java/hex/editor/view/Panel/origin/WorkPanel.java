package hex.editor.view.Panel.origin;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import hex.editor.services.TableViewer;
import hex.editor.view.Style.IStyleSheet;

public class WorkPanel extends BasePanel {
    private JTable table;
    private IStyleSheet styleSheet = super.getStyleSheet();

    public WorkPanel(int height, int width) {
        super(height, width);
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

    public void showData(String[] data) {

        int colomns_count = 10;

        String[] colomns = new String[colomns_count];
        for (int i = 0 ; i < colomns_count; i++) colomns[i] = String.valueOf(i);
        
        String[][] box = TableViewer.getTable(data, colomns_count);

        table = new JTable(box, colomns);
        // Настройка таблицы
        table.setRowHeight(30);
        table.setRowHeight(10, 20);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowVerticalLines(true);
        table.setGridColor(styleSheet.getMainTextColor());
        table.setBackground(styleSheet.getBackBaseColor());
        table.setForeground(styleSheet.getMainTextColor());

        this.add(table, BorderLayout.CENTER);
    }
}
