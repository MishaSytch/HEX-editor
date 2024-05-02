package hex.editor.view.Style;

import java.awt.Color;

public class StyleSheet_MainWindow implements IStyleSheet {
    private static final double LEFT_SIZE = 0.25;
    private static final double RIGHT_SIZE = 0.15;
    private static final double HEAD_SIZE = 0.04;
    private static final double BOTTOM_SIZE = 0.15;


    private static final Color BackMainColor = new Color(30, 30, 30);
    private static final Color BackNotMainColor = new Color(25, 25, 25);
    private static final Color ForeMainColor = new Color(25, 15, 25);
    private static final Color ForeNotMainColor = new Color(25, 15, 25);
    private static final Color MainTextColor = new Color(240, 240, 240);
    private static final Color NotMainTextColor = new Color(150, 150, 150);
    private static final Color SelectedColor = new Color(200, 200, 0);
    private static final Color ToolTipBackColor = new Color(50, 0, 0);
    private static final Color ToolTipTextColor = new Color(255, 255, 0);
    @Override
    public double getLeftPanelSize() {
        return LEFT_SIZE;    
    }
    @Override
    public double getRightPanelSize() {
        return RIGHT_SIZE;
    }
    @Override
    public double getHeadPanelSize() {
        return HEAD_SIZE;
    }
    @Override
    public double getBottomPanelSize() {
        return BOTTOM_SIZE;
    }
    @Override
    public Color getBackBaseColor() {
        return BackMainColor;    
    }
    @Override
    public Color getBackSecondaryColor() {
        return BackNotMainColor;
    }
    @Override
    public Color getForeBaseColor() {
        return ForeMainColor;
    }
    @Override
    public Color getForeSecondaryColor() {
        return ForeNotMainColor;
    }
    @Override
    public Color getMainTextColor() {
        return MainTextColor;
    }
    @Override
    public Color getSecondaryTextColor() {
        return NotMainTextColor;
    }
    @Override
    public Color getSelectedColor() {
        return SelectedColor;
    }
    @Override
    public Color getToolTipBackColor() {
        return ToolTipBackColor;
    }
    @Override
    public Color getToolTipTextColor() {
        return ToolTipTextColor;
    }
}
