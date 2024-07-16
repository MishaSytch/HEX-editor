package hex.editor.view.Panel;

import hex.editor.view.MainWindow;
import hex.editor.view.Panel.origin.WorkPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuBarTest {

    @Test
    public void testMenuBarConstructor() {
        WorkPanel mockWorkPanel = new WorkPanel(new MainWindow(), new InfoPanel(new MainWindow()));

        assertDoesNotThrow(() -> new MenuBar(mockWorkPanel));


    }
}