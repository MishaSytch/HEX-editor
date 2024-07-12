package hex.editor.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowTest {

    @Test
    void testConstructor() {
        assertDoesNotThrow(MainWindow::new);
    }

}