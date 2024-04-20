package hex.editor.view.Style;

import java.awt.Color;

public class StyleSheet_MainWindow implements IStyleSheet {
    private static double LEFT_SIZE = 0.25;
    private static double RIGHT_SIZE = 0.15;
    private static double HEAD_SIZE = 0.04;
    private static double BOTTOM_SIZE = 0.15;


    private static Color BackMainColor = new Color(30, 30, 30);
    private static Color BackNotMainColor = new Color(25, 25, 25);
    private static Color ForeMainColor = new Color(25, 15, 25);
    private static Color ForeNotMainColor = new Color(25, 15, 25);
    private static Color MainTextColor = new Color(240, 240, 240);
    private static Color NotMainTextColor = new Color(150, 150, 150);
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
}
