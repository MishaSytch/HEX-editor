package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;

import java.util.Map;
import java.util.concurrent.Exchanger;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import hex.editor.model.Info;
import hex.editor.model.Types;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;

public class InfoPanel extends BasePanel {
    private JTextField search;
    private JLabel info;
    private MainWindow mainWindow;
    private JPanel panel;
    private JButton maskButton;
    private JButton hexButton;
    private Exchanger<Object> SEARCH_BY_HEX_Exchanger;
    private Exchanger<Object> SEARCH_BY_STRING_Exchanger;

    public InfoPanel(MainWindow mainWindow, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));

        this.mainWindow = mainWindow;
        this.SEARCH_BY_HEX_Exchanger = exchangers.get(Types.SEARCH_BY_HEX);
        this.SEARCH_BY_STRING_Exchanger = exchangers.get(Types.SEARCH_BY_STRING); 

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 5, 10, 0));


        updateSearch();        
        this.add(search, BorderLayout.SOUTH);
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
        this.add(search, BorderLayout.SOUTH); 
    }

    public void showSearchWindow() {
        this.remove(search);
        panel = new JPanel(); 
        panel.setLayout(new BorderLayout());  
        
        maskButton = getSearchButton("Search by mask");

        hexButton = getSearchButton("Search by Hex");

        panel.add(maskButton, BorderLayout.NORTH);
        panel.add(hexButton, BorderLayout.SOUTH);

        
        
        JTextField searchByMask = new JTextField();
        search.setEditable(false);
        search.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(panel, BorderLayout.SOUTH);
        
        search.setText("");
        search.setEditable(true);

        System.out.println("View: ready to search");

        this.add(panel, BorderLayout.SOUTH);
    }

    private void updateSearch() {
        if (search != null) this.remove(search);
        if (panel != null) this.remove(panel);
        
        info = new JLabel();
        this.add(info, BorderLayout.WEST);  
        search = new JTextField("To search press: ctrl + S");
        search.setEditable(false);
        search.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JButton getSearchButton(String title) {
        JButton button = new JButton(title);
        button.setBackground(getBackground());
        button.setForeground(Color.WHITE);
        button.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (button == maskButton){
                    maskButton.setEnabled(false);
                    hexButton.setEnabled(true);
                } else {
                    hexButton.setEnabled(false);
                    maskButton.setEnabled(true);
                }
                panel.remove(search);
                search.removeAll();
                search.setEditable(true);
                search.setBorder(new EmptyBorder(10, 10, 10, 10));
                search.setText("");
                panel.add(search);

                SwingUtilities.updateComponentTreeUI(panel);
                
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

                            if (button == maskButton){
                                try {
                                    SEARCH_BY_STRING_Exchanger.exchange(search.getText());
                                } catch (InterruptedException e) {
                                }

                            } else {
                                try {
                                    SEARCH_BY_HEX_Exchanger.exchange(search.getText());
                                } catch (InterruptedException e) {
                                }
                                
                            }

        
                            System.out.println("View: searching...");
                        }
                        }

                    @Override
                    public void keyTyped(KeyEvent arg0) {
                    }
                    
                });

            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
            
        });

        return button;
    }
}
