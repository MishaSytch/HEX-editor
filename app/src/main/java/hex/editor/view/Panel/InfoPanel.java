package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import hex.editor.model.Info;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;

public class InfoPanel extends BasePanel {
    private JTextField search;
    private JLabel info;
    private MainWindow mainWindow;

    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));
        this.mainWindow = mainWindow;

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 5, 10, 0));


        updateSearch();
    }

    public void setInfo(Info info_Info) {
        this.removeAll();
        updateSearch();
        info = getText(
            "<html>"
            + "Row: " + info_Info.getRow()
            + "<br>"
            + "Column: " + info_Info.getColumn()
            + "<br>"
            + "<br>"
            + "<br>"
            + "Char: " + info_Info.getChar_info()
            + "<br>"
            + "Hex: " + info_Info.getHex_info()
        );
        this.add(info, BorderLayout.WEST);  
    }

    public void showSearchWindow() {
        this.remove(search);
        search.setText("");
        search.setEditable(true);

        System.out.println("View: ready to search");

        this.add(search, BorderLayout.SOUTH);
        
        search.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    updateSearch();
                }
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    search.setEditable(false);

                    System.out.println("View: searching...");
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
            
        });
    }

    private void updateSearch() {
        if (search != null) this.remove(search);
        
        info = new JLabel();
        this.add(info, BorderLayout.WEST);  
        search = new JTextField("To search press: ctrl + S");
        search.setEditable(false);
        search.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(search, BorderLayout.SOUTH);
    }
}
