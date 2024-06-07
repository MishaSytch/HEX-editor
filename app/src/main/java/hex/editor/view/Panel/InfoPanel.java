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
import javax.swing.SwingUtilities;
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

    private final JPanel searchingPanel = new JPanel();
    private JButton maskButton;
    private JButton hexButton;
    private JButton clearButton;
    private JButton nextPosButton;
    private JButton previousPosButton;
    private WorkPanel workPanel;
    private final JPanel positionControlPanel = new JPanel();

    public InfoPanel(MainWindow mainWindow) {
        super(mainWindow.getHeight(), (int)(mainWindow.getWidth() * 0.2));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 5, 10, 0));
        initComponents();
    }

    public void removeInfo() {
        info.setText("");
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void setInfo(Info info_Info) {
        info.setText(info_Info.getInfo());
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setInfo(List<Info> info_Infos) {
        if (info_Infos.size() == 2 || info_Infos.size() == 4 || info_Infos.size() == 8) {
            info.setText(Info.getBytes(info_Infos));
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setWorkPanel(WorkPanel workPanel) {
        this.workPanel = workPanel;
    }

    public void start() {
        condition(Condition.START);
    }
    
    private void initComponents() {
        add(info, BorderLayout.WEST);
        searchingField.setBorder(new EmptyBorder(10, 10, 10, 10));
        maskButton = getButton( "Search by mask");
        hexButton = getButton( "Search by Hex");
        clearButton = getButton("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (workPanel != null) {
                    workPanel.unselectCell();
                    condition(Condition.START);
                }
            }
        });
        nextPosButton = getButton("Next");
        nextPosButton.addActionListener(e -> workPanel.nextPosition());
        previousPosButton = getButton("Previous");
        previousPosButton.addActionListener(e -> workPanel.previousPosition());

        searchingPanel.setLayout(new GridLayout(4, 1));
        searchingPanel.add(hexButton);
        searchingPanel.add(maskButton);
        searchingPanel.add(searchingField);
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
        condition(Condition.START);

        searchingField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (workPanel.getHex() != null) {
                    if (!(hexButton.isEnabled() || maskButton.isEnabled())) {
                        condition(Condition.TYPE);
                    } else {
                        searchingField.setText("");
                    }
                    SwingUtilities.updateComponentTreeUI(searchingPanel);
                }
            }
        });

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
                    SwingUtilities.updateComponentTreeUI(searchingPanel);
                }
            }
        });
    }
    
    private void condition(Condition condition) {
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

                previousPosButton.setVisible(false);
                nextPosButton.setVisible(false);
                clearButton.setVisible(false);

                SwingUtilities.updateComponentTreeUI(this);
                break;
            }
            case TYPE: {
                hexButton.setVisible(true);
                hexButton.setEnabled(true);
                maskButton.setVisible(true);
                maskButton.setEnabled(true);
                searchingField.setVisible(true);
                searchingField.setEnabled(true);
                searchingField.setForeground(getStyleSheet().getBackBaseColor());
                searchingField.setBackground(getStyleSheet().getMainTextColor());

                previousPosButton.setVisible(false);
                nextPosButton.setVisible(false);
                clearButton.setVisible(false);

                SwingUtilities.updateComponentTreeUI(this);
                break;
            }
            case SHOW: {
                hexButton.setVisible(false);
                hexButton.setEnabled(false);
                maskButton.setVisible(false);
                maskButton.setEnabled(false);
                searchingField.setVisible(false);
                searchingField.setEnabled(false);
                searchingField.setBackground(getBackground());
                searchingField.setForeground(getStyleSheet().getMainTextColor());

                previousPosButton.setVisible(true);
                nextPosButton.setVisible(true);
                clearButton.setVisible(true);

                SwingUtilities.updateComponentTreeUI(this);
                break;
            }
        }
    }

    private JButton getButton(String title) {
        JButton button = new JButton(title);
        button.setBackground(getBackground());
        button.setForeground(getStyleSheet().getMainTextColor());

        button.addActionListener(e -> {
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
            searchingField.setText("Type to use " + methodInfo);
            searchingField.setBackground(getBackground());
            searchingField.setForeground(getStyleSheet().getMainTextColor());
            SwingUtilities.updateComponentTreeUI(searchingPanel);
        });
        return button;
    }
}
