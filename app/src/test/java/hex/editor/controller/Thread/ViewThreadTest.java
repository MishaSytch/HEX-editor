package hex.editor.controller.Thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ViewThreadTest {

    @Test
    void getInstance() {
        ViewThread viewThread = ViewThread.getInstance();

        Assertions.assertEquals(viewThread, ViewThread.getInstance());
    }
}