package hex.editor.view.Panel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.WorkPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuBarTest {

    @Test
    public void testMenuBarConstructor() {
        // Create a mock WorkPanel object for testing
        WorkPanel mockWorkPanel = new WorkPanel(new MainWindow(), new InfoPanel(new MainWindow()));

        // Create a MenuBar object for testing
        assertDoesNotThrow(() -> new MenuBar(mockWorkPanel));


    }
}