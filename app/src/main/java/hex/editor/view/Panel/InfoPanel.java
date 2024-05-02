package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import hex.editor.view.Panel.origin.WorkPanel;

public class InfoPanel extends BasePanel {
    private JTextField search;
    private JLabel info;
    public JLabel getInfo() {
        return info;
    }

    private JPanel panel;
    private JButton maskButton;
    private JButton hexButton;
    private Exchanger<Object> SEARCH_BY_HEX_Exchanger;
    private Exchanger<Object> SEARCH_BY_STRING_Exchanger;
    private WorkPanel workPanel;

    public InfoPanel(MainWindow mainWindow, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));

        this.SEARCH_BY_HEX_Exchanger = exchangers.get(Types.SEARCH_BY_HEX);
        this.SEARCH_BY_STRING_Exchanger = exchangers.get(Types.SEARCH_BY_STRING); 

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 5, 10, 0));


        updateSearch();        
        this.add(search, BorderLayout.SOUTH);
    }

    public void setInfo(Info info_Info) {
        updateSearch();
        info = getText(info_Info.getInfo());
        this.add(info, BorderLayout.WEST); 
        this.add(search, BorderLayout.SOUTH); 
    }

    public void showSearchWindow() {
        this.remove(search);
        
        panel = new JPanel(); 
        panel.setLayout(new GridLayout(2, 1)); 
        panel.setPreferredSize(new Dimension(getWidth(), (int)(getHeight() * 0.2)));
        
        maskButton = getSearchButton("Search by mask");

        hexButton = getSearchButton("Search by Hex");

        panel.add(maskButton);
        panel.add(hexButton);
        
        this.add(panel, BorderLayout.SOUTH);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateSearch() {
        if (search != null) this.removeAll();
        
        search = new JTextField("To search type there");
        search.setEditable(false);
        search.setBorder(new EmptyBorder(10, 10, 10, 10));
        search.addMouseListener((MouseListener) new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSearchWindow();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });
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
                panel.setLayout(new GridLayout(3, 1));
                search.removeAll();
                search.setEditable(false);
                search.setText("Type to search...");
                search.setBorder(getBorder());
                panel.add(search);

                SwingUtilities.updateComponentTreeUI(panel);
                search.addMouseListener(new MouseListener() {

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
                        System.out.println("View: ready to search");
                        search.setEditable(true);
                        search.setText("");
                    }

                    @Override
                    public void mouseReleased(MouseEvent arg0) {
                    }
                    
                });

                search.addKeyListener(new KeyListener() {

                    @Override
                    public void keyPressed(KeyEvent arg0) {
                    }

                    @Override
                    public void keyReleased(KeyEvent arg0) {
                        if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            updateSearch();
                            workPanel.unselectCell();
                        }
                        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {  
                            if (!search.getText().isEmpty()) {
                                if (button.getText().equals(maskButton.getText())){
                                    try {
                                        SEARCH_BY_STRING_Exchanger.exchange(search.getText().trim(), 1500, TimeUnit.MILLISECONDS);
                                    } catch (InterruptedException | TimeoutException e) {}
    
                                } 
                                System.out.println(button.getText());
                                if (button.getText().equals(hexButton.getText())) {
                                    try {
                                        SEARCH_BY_HEX_Exchanger.exchange(Arrays.stream(search.getText().replaceAll(",", " ").replaceAll(";", " ").split(" ")).toList(), 1500, TimeUnit.MILLISECONDS);
                                    } catch (InterruptedException | TimeoutException e) {}
                                    
                                }     
                                if (workPanel != null) {
                                    System.out.println("View: smt sent");
                                    workPanel.waitPosition();
                                }
                            }
                            SwingUtilities.updateComponentTreeUI(panel);
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

    public void setWorkPanel(WorkPanel workPanel) {
        this.workPanel = workPanel;
    }
}
