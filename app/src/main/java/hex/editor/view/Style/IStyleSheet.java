package hex.editor.view.Style;

import java.awt.Color;

public interface IStyleSheet {
    double getLeftPanelSize();
    double getRightPanelSize();
    double getHeadPanelSize();
    double getBottomPanelSize();

    Color getBackBaseColor();
    Color getBackSecondaryColor();
    Color getForeBaseColor();
    Color getForeSecondaryColor();

    Color getMainTextColor();
    Color getSecondaryTextColor();
    Color getSelectedColor();
    Color getToolTipBackColor();
    Color getToolTipTextColor();
}
