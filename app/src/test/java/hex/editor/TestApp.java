package hex.editor;

import java.awt.EventQueue;
import javax.swing.JFrame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hex.editor.view.MainWindow;

public class TestApp {
    @Test
    void testappExecuted() {
        App app = new App();

        Assertions.assertNotNull(app);
    }

    @Test
    void startApp() {
        

    }
}
