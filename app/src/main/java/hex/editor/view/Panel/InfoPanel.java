package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
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
import javax.swing.border.LineBorder;

import hex.editor.model.Info;
import hex.editor.model.Types;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;
import hex.editor.view.Panel.origin.WorkPanel;

public class InfoPanel extends BasePanel {
    private final JTextField search = new JTextField();
    private final JLabel info = getText("");
    public JLabel getInfo() {
        return info;
    }

    private final JPanel panel = new JPanel();
    private JButton maskButton;
    private JButton hexButton;
    private JButton clearButton;
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
        maskButton = getSearchButton("Search by mask");
        hexButton = getSearchButton("Search by Hex");
        clearButton = getSearchButton("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (workPanel != null) workPanel.unselectCell();
                if (panel.getComponents().length != 0) start();
            }
        });

        panel.setLayout(new GridLayout(4, 1));
        panel.add(hexButton);
        panel.add(maskButton);
        panel.add(search);

        panel.setBackground(getBackground());
        panel.setForeground(getBackground());
        panel.setVisible(true);

        add(panel, BorderLayout.SOUTH);
        start();
        search.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (workPanel.getHex() != null) {
                    if (!(hexButton.isEnabled() || maskButton.isEnabled())) {
                        search.setText("Chose method");
                        hexButton.setEnabled(true);
                        maskButton.setEnabled(true);
                        hexButton.setVisible(true);
                        maskButton.setVisible(true);
                    } else {
                        System.out.println("View: ready to search");
                        search.setEditable(true);
                        search.setBackground(getStyleSheet().getMainTextColor());
                        search.setForeground(Color.BLACK);
                        search.setText("");
                    }
                    SwingUtilities.updateComponentTreeUI(panel);
                }
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
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!search.getText().trim().isEmpty()) {
                        try {
                            if (!maskButton.isEnabled()){
                                SEARCH_BY_STRING_Exchanger.exchange(search.getText().trim(), 1500, TimeUnit.MILLISECONDS);
                            }
                            if (!hexButton.isEnabled()) {
                                SEARCH_BY_HEX_Exchanger.exchange(Arrays.stream(search.getText().split("[\\t\\s\\W+]")).collect(Collectors.toList()), 1500, TimeUnit.MILLISECONDS);
                            }
                        } catch (InterruptedException | TimeoutException e) {
                            System.out.println("View: searching error");
                        }
                        if (workPanel != null) {
                            System.out.println("View: smt sent");
                            workPanel.waitPosition();
                        }
                    }
                    clearButton.setVisible(true);
                    clearButton.setEnabled(true);
                    SwingUtilities.updateComponentTreeUI(panel);
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
    }

    private void start() {
        hexButton.setVisible(false);
        hexButton.setEnabled(false);
        maskButton.setVisible(false);
        maskButton.setEnabled(false);
        search.setText("Searching...");
        search.setEnabled(false);
        search.setVisible(true);
        search.setBackground(getBackground());
        search.setForeground(getStyleSheet().getMainTextColor());

        clearButton.setVisible(false);
        clearButton.setEnabled(false);
    }

    private JButton getSearchButton(String title) {
        JButton button = new JButton(title);
        button.setBackground(getBackground());
        button.setForeground(getStyleSheet().getMainTextColor());
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
                String methodInfo;
                if (button == maskButton){
                    maskButton.setEnabled(false);
                    hexButton.setEnabled(true);
                    methodInfo = "Mask method";
                } else {
                    hexButton.setEnabled(false);
                    maskButton.setEnabled(true);
                    methodInfo = "Hex method";
                }
                search.setText("Type to use " + methodInfo);
                search.setBackground(getBackground());
                search.setForeground(getStyleSheet().getMainTextColor());
                SwingUtilities.updateComponentTreeUI(panel);

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
