package hex.editor.view.Panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import hex.editor.model.Condition;
import hex.editor.model.Info;
import hex.editor.services.FileWriter;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.BasePanel;
import hex.editor.view.Panel.origin.WorkPanel;

public class InfoPanel extends BasePanel {
    private final JTextField searchingField = new JTextField();
    private final JLabel info = getText("");
    public JLabel getInfo() {
        return info;
    }

    private final JPanel searchingPanel;
    private final JButton maskButton;
    private final JButton hexButton;
    private final JButton clearButton;
    private final JButton nextPosButton;
    private final JButton previousPosButton;
    private final JPanel positionControlPanel;
    private WorkPanel workPanel;

    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));


        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 5, 10, 0));

        maskButton = getButton( "Search by mask");
        hexButton = getButton( "Search by Hex");
        clearButton = getButton("Clear");
        nextPosButton = getButton("Next");
        previousPosButton = getButton("Previous");

        searchingField.setBorder(new EmptyBorder(10, 10, 10, 10));

        searchingPanel = new JPanel();
        
        searchingPanel.setLayout(new GridLayout(4, 1));
        searchingPanel.add(hexButton);
        searchingPanel.add(maskButton);
        searchingPanel.add(searchingField);
        positionControlPanel = new JPanel();
        searchingPanel.add(positionControlPanel);
        
        positionControlPanel.setLayout(new GridLayout(1, 3));
        positionControlPanel.add(previousPosButton);
        positionControlPanel.add(clearButton);
        positionControlPanel.add(nextPosButton);
        positionControlPanel.setVisible(true);
        positionControlPanel.setBackground(getBackground());
        
        searchingPanel.setBackground(getStyleSheet().getBackBaseColor());
        searchingPanel.setForeground(getStyleSheet().getBackBaseColor());
        
        add(searchingPanel, BorderLayout.SOUTH);
        add(info, BorderLayout.WEST);

        condition(Condition.START);
    }

    public void setWorkPanel(WorkPanel workPanel) {
        this.workPanel = workPanel;

        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (workPanel != null) {
                    workPanel.unselectCell();
                    condition(Condition.START);
                }
            }
        });
        nextPosButton.addActionListener(e -> workPanel.nextPosition());
        previousPosButton.addActionListener(e -> workPanel.previousPosition());
    }

    public void removeInfo() {
        info.setText("");
    }
    
    public void setInfo(Info info_Info) {
        info.setText(info_Info.getInfo());
    }

    public void setInfo(List<Info> info_Infos) {
        if (info_Infos.size() == 2 || info_Infos.size() == 4 || info_Infos.size() == 8) {
            info.setText(Info.getBytes(info_Infos));
        } else {
            info.setText("");
        }
    }

    public void start() {
        condition(Condition.START);
    }
    
    private void condition(Condition condition) {
        for (MouseListener m : searchingField.getMouseListeners()) {
            searchingField.removeMouseListener(m);
        }
        for (KeyListener k : searchingField.getKeyListeners()) {
            searchingField.removeKeyListener(k);
        }

        switch (condition) {
            case START: {
                hexButton.setVisible(false);
                hexButton.setEnabled(false);

                maskButton.setVisible(false);
                maskButton.setEnabled(false);

                searchingField.setVisible(true);
                searchingField.setEnabled(false);
                searchingField.setText("Type to search...");
                searchingField.setBackground(getBackground());
                searchingField.setForeground(getStyleSheet().getMainTextColor());
                searchingField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (workPanel.getHex() != null) {
                            condition(Condition.CHOOSE);
                        }
                    }
                });

                previousPosButton.setVisible(false);

                nextPosButton.setVisible(false);

                clearButton.setVisible(false);

                break;
            }
            case CHOOSE: {
                hexButton.setVisible(true);
                hexButton.setEnabled(true);

                maskButton.setVisible(true);
                maskButton.setEnabled(true);

                searchingField.setVisible(true);
                searchingField.setEnabled(true);
                searchingField.setText("Choose method");
                searchingField.setBackground(getBackground());
                searchingField.setForeground(getStyleSheet().getMainTextColor());

                previousPosButton.setVisible(false);

                nextPosButton.setVisible(false);

                clearButton.setVisible(false);

                searchingField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (workPanel.getHex() != null) {
                            searchingField.setText("");
                        }
                    }
                });

                break;
            }
            case TYPE: {
                hexButton.setVisible(true);

                maskButton.setVisible(true);

                searchingField.setVisible(true);
                searchingField.setEnabled(true);
                searchingField.setText("");
                searchingField.setBackground(getBackground());
                searchingField.setForeground(getStyleSheet().getMainTextColor());
                searchingField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent arg0) {
                        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (!searchingField.getText().trim().isEmpty()) {
                                condition(Condition.SHOW);
                                if (!maskButton.isEnabled()){
                                    workPanel.searchByMask(searchingField.getText().trim());
                                }
                                if (!hexButton.isEnabled()) {
                                    workPanel.searchByHex(Arrays.stream(searchingField.getText().split(FileWriter.getRegexForSplit())).collect(Collectors.toList()));
                                }
                            }
                        }
                    }
                });

                previousPosButton.setVisible(false);

                nextPosButton.setVisible(false);

                clearButton.setVisible(false);

                break;
            }
            case SHOW: {
                hexButton.setVisible(false);

                maskButton.setVisible(false);

                searchingField.setVisible(false);

                previousPosButton.setVisible(true);

                nextPosButton.setVisible(true);

                clearButton.setVisible(true);
                
                break;
            }
        }
    }

    private JButton getButton(String title) {
        JButton button = new JButton(title);
        button.setBackground(getBackground());
        button.setForeground(getStyleSheet().getMainTextColor());

        button.addActionListener(e -> {
            if (button == maskButton){
                hexButton.setEnabled(true);
                
                maskButton.setEnabled(false);
            } else {
                hexButton.setEnabled(false);

                maskButton.setEnabled(true);
            }

            condition(Condition.TYPE);
        });
        
        return button;
    }


}
