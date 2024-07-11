package hex.editor.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CacheLinesTest {
    CacheLines cacheLines;
    List<List<String>> list = new ArrayList<>();
    String data;

    @BeforeEach
    void setUp() {
        data = "48 65 6C 6C 6f 20 57 6f 72 6C 64 21 ";
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines = new CacheLines(list, 1);
    }

    @Test
    void getData() {
        Assertions.assertEquals(list, cacheLines.getData());
        Assertions.assertEquals(1, cacheLines.getPart());
        Assertions.assertFalse(cacheLines.isModified());
    }

    @Test
    void updateData() {
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines.updateData(list);

        Assertions.assertEquals(list, cacheLines.getData());
        Assertions.assertEquals(1, cacheLines.getPart());
        Assertions.assertTrue(cacheLines.isModified());
    }

    @Test
    void getLength() {
        Assertions.assertEquals(data.length(), cacheLines.getLength());
    }

    @Test
    void getNextIndex() {
        Assertions.assertEquals(data.length(), cacheLines.getNextIndex());
    }

    @Test
    void getPreviousIndex() {
        Assertions.assertEquals(0, cacheLines.getPreviousIndex());
    }

    @Test
    void getIndex() {
        Assertions.assertEquals(0, cacheLines.getIndex());
    }

    @Test
    void isModified() {
        Assertions.assertFalse(cacheLines.isModified());
        cacheLines.updateData(null);
        Assertions.assertTrue(cacheLines.isModified());
    }

    @Test
    void getPart() {
        Assertions.assertEquals(1, cacheLines.getPart());
    }

    @Test
    void getSize() {
        Assertions.assertEquals(12, cacheLines.getSize());
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines.updateData(list);
        Assertions.assertEquals(24, cacheLines.getSize());
    }
}