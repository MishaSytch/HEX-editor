package hex.editor.view;

import org.junit.jupiter.api.*;

public class TestMainWindow {
    
    @Test
    void testMainWindow() {
        MainWindow mainWindow = new MainWindow();

        Assertions.assertNotNull(mainWindow);
    }
}
