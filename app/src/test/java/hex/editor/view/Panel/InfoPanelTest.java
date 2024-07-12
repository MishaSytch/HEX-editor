package hex.editor.view.Panel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.WorkPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoPanelTest {

    private InfoPanel infoPanel;

    @BeforeEach
    void setUp() {
        infoPanel = new InfoPanel(new MainWindow());
        infoPanel.setWorkPanel(new WorkPanel(new MainWindow(), infoPanel));
    }

    @Test
    public void testRemoveInfo() {
        assertDoesNotThrow(() -> infoPanel.removeInfo());
    }
}