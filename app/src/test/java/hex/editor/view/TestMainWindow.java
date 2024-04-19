package hex.editor.view;

import org.junit.gen5.api.Assertions;
import org.junit.gen5.api.Test;

public class TestMainWindow {
    
    @Test
    void testMainWindow() {
        MainWindow mainWindow = new MainWindow();

        Assertions.assertNotNull(mainWindow);
    }
}
