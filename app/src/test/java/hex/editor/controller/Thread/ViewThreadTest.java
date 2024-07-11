package hex.editor.controller.Thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ViewThreadTest {

    @Test
    void getInstance() {
        ViewThread viewThread = ViewThread.getInstance();

        Assertions.assertEquals(viewThread, ViewThread.getInstance());
    }

    @Test
    void getInstance_singleton() {
        Thread first = new Thread(new Runnable() {
            @Override
            public void run() {
                ViewThread vt = ViewThread.getInstance();
            }
        });

        Thread second = new Thread(new Runnable() {
            @Override
            public void run() {
                ViewThread vt = ViewThread.getInstance();
            }
        });

        first.run();
        second.run();

    }
}