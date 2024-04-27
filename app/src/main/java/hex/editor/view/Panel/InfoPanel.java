package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import hex.editor.model.Info;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;

public class InfoPanel extends BasePanel {
    private JLabel search;

    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 5, 10, 0));
        search = new JLabel("FFF");
        this.add(search);
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

    public void showSearchWindow() {
        JTextField text = new JTextField("FFF");
        
        System.out.println("View: ready to search");

        search.add(text);
        SwingUtilities.updateComponentTreeUI(this);
        
        search.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    search.removeAll();
                }
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("View: searching...");
                }
            }
            
        });
    }
}
