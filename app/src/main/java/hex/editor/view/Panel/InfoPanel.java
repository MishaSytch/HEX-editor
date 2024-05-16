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
import java.util.stream.Collectors;

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
    private final JTextField search = new JTextField("To search type there");
    private final JLabel info = getText("");
    public JLabel getInfo() {
        return info;
    }

    private final JPanel panel = new JPanel();
    private JButton maskButton;
    private JButton hexButton;
    private final Exchanger<Object> SEARCH_BY_HEX_Exchanger;
    private final Exchanger<Object> SEARCH_BY_STRING_Exchanger;
    private WorkPanel workPanel;

    public InfoPanel(MainWindow mainWindow, Map<Types, Exchanger<Object>> exchangers) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));

        this.SEARCH_BY_HEX_Exchanger = exchangers.get(Types.SEARCH_BY_HEX);
        this.SEARCH_BY_STRING_Exchanger = exchangers.get(Types.SEARCH_BY_STRING); 

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 5, 10, 0));
        initComponents();
    }

    public void setInfo(Info info_Info) {
        info.setText(info_Info.getInfo());
        SwingUtilities.updateComponentTreeUI(this);
    }



    private void initComponents() {
        add(info, BorderLayout.WEST);
        search.setBorder(new EmptyBorder(10, 10, 10, 10));
        search.setEnabled(false);
        search.setVisible(true);
        search.setBackground(getBackground());
        search.setForeground(getStyleSheet().getMainTextColor());

        maskButton = getSearchButton("Search by mask");
        hexButton = getSearchButton("Search by Hex");

        panel.add(search, BorderLayout.SOUTH);
        panel.add(maskButton, BorderLayout.CENTER);
        panel.add(hexButton, BorderLayout.NORTH);

        panel.setPreferredSize(new Dimension(getWidth(), (int)(getHeight() * 0.3)));
        panel.setVisible(true);

        add(panel, BorderLayout.SOUTH);

        search.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hexButton.setEnabled(true);
                maskButton.setEnabled(true);
                hexButton.setVisible(true);
                maskButton.setVisible(true);
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
        button.setVisible(false);
        button.setEnabled(false);

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
                workPanel.updateData();
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
                                        SEARCH_BY_HEX_Exchanger.exchange(Arrays.stream(search.getText().split("[\\t\\s\\W+]")).collect(Collectors.toList()), 1500, TimeUnit.MILLISECONDS);
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
