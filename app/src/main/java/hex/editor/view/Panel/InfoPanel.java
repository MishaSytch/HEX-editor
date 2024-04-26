package hex.editor.view.Panel;

import java.awt.BorderLayout;

import javax.swing.border.EmptyBorder;

import hex.editor.model.Info;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;

public class InfoPanel extends BasePanel {

    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 5, 10, 0));
    }

    public void setInfo(Info info) {
        this.removeAll();
        this.add(getText(
            "<html>"
            + "Row: " + info.getRow()
            + "<br>"
            + "Column: " + info.getColumn()
            + "<br>"
            + "<br>"
            + "<br>"
            + "Char: " + info.getChar_info()
            + "<br>"
            + "Hex: " + info.getHex_info()
            ), BorderLayout.WEST
        );  
    }
}
