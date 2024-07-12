package hex.editor.view.Frame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

import hex.editor.view.Style.IStyleSheet;
import hex.editor.view.Style.StyleSheet_MainWindow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BaseFrameTest {

    private BaseFrame baseFrame;

    @BeforeEach
    public void setUp() {
        IStyleSheet styleSheet = mock(StyleSheet_MainWindow.class);
        when(styleSheet.getBackBaseColor()).thenReturn(new Color(30, 30, 30));
        when(styleSheet.getForeBaseColor()).thenReturn(new Color(25, 15, 25));

        baseFrame = new BaseFrame(600, 800);
    }

    @Test
    public void testGetHeight() {
        assertEquals(600, baseFrame.getHEIGHT());
    }

    @Test
    public void testGetWidth() {
        assertEquals(800, baseFrame.getWIDTH());
    }

    @Test
    public void testPreferredSize() {
        Dimension expectedDimension = new Dimension(800, 600);
        assertEquals(expectedDimension, baseFrame.getPreferredSize());
    }

    @Test
    public void testBackgroundAndForegroundColors() {
        assertEquals(new Color(30, 30, 30), baseFrame.getBackground());
        assertEquals(new Color(25, 15, 25), baseFrame.getForeground());
    }

    @Test
    public void testLayout() {
        assertEquals(BorderLayout.class, baseFrame.getLayout().getClass());
    }
}