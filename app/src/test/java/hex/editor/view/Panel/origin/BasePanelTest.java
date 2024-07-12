package hex.editor.view.Panel.origin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.swing.JLabel;

public class BasePanelTest {

    @Test
    public void testGetStyleSheet() {
        BasePanel basePanel = new BasePanel(100, 200);
        assertNotNull(basePanel.getStyleSheet());
    }

    @Test
    public void testBasePanelConstructor() {
        BasePanel basePanel = new BasePanel(100, 200);
        assertEquals(200, basePanel.getPreferredSize().width);
        assertEquals(100, basePanel.getPreferredSize().height);
        assertEquals(basePanel.getStyleSheet().getBackBaseColor(), basePanel.getBackground());
        assertEquals(basePanel.getStyleSheet().getForeBaseColor(), basePanel.getForeground());
    }

    @Test
    public void testGetText() {
        BasePanel basePanel = new BasePanel(100, 200);
        JLabel text = basePanel.getText("Test Info");
        assertNotNull(text);
        assertEquals("Test Info", text.getText());
        assertEquals(basePanel.getStyleSheet().getMainTextColor(), text.getForeground());
    }
}