package hex.editor.view.Style;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleSheet_MainWindowTest {

    private IStyleSheet styleSheet;

    @BeforeEach
    public void setUp() {
        styleSheet = new StyleSheet_MainWindow();
    }

    @Test
    public void testGetLeftPanelSize() {
        assertEquals(0.25, styleSheet.getLeftPanelSize());
    }

    @Test
    public void testGetRightPanelSize() {
        assertEquals(0.15, styleSheet.getRightPanelSize());
    }

    @Test
    public void testGetHeadPanelSize() {
        assertEquals(0.04, styleSheet.getHeadPanelSize());
    }

    @Test
    public void testGetBottomPanelSize() {
        assertEquals(0.15, styleSheet.getBottomPanelSize());
    }

    @Test
    public void testGetBackBaseColor() {
        assertEquals(new Color(30, 30, 30), styleSheet.getBackBaseColor());
    }

    @Test
    public void testGetBackSecondaryColor() {
        assertEquals(new Color(25, 25, 25), styleSheet.getBackSecondaryColor());
    }

    @Test
    public void testGetForeBaseColor() {
        assertEquals(new Color(25, 15, 25), styleSheet.getForeBaseColor());
    }

    @Test
    public void testGetForeSecondaryColor() {
        assertEquals(new Color(25, 15, 25), styleSheet.getForeSecondaryColor());
    }

    @Test
    public void testGetMainTextColor() {
        assertEquals(new Color(240, 240, 240), styleSheet.getMainTextColor());
    }

    @Test
    public void testGetSecondaryTextColor() {
        assertEquals(new Color(150, 150, 150), styleSheet.getSecondaryTextColor());
    }

    @Test
    public void testGetSelectedColor() {
        assertEquals(new Color(200, 200, 0), styleSheet.getSelectedColor());
    }

    @Test
    public void testGetToolTipBackColor() {
        assertEquals(new Color(50, 0, 0), styleSheet.getToolTipBackColor());
    }

    @Test
    public void testGetToolTipTextColor() {
        assertEquals(new Color(255, 255, 0), styleSheet.getToolTipTextColor());
    }
}