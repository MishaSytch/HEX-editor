package hex.editor.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CacheLinesTest {
    CacheLines cacheLines;
    List<List<String>> list = new ArrayList<>();
    String data;

    @BeforeEach
    void testSetUp() {
        data = "48 65 6C 6C 6f 20 57 6f 72 6C 64 21 ";
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines = new CacheLines(list, 1);
    }

    @Test
    void testGetData() {
        Assertions.assertEquals(list, cacheLines.getData());
        Assertions.assertEquals(1, cacheLines.getPart());
        Assertions.assertFalse(cacheLines.isModified());
    }

    @Test
    void testUpdateData() {
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines.updateData(list);

        Assertions.assertEquals(list, cacheLines.getData());
        Assertions.assertEquals(1, cacheLines.getPart());
        Assertions.assertTrue(cacheLines.isModified());
    }

    @Test
    void testGetLength() {
        Assertions.assertEquals(data.length(), cacheLines.getLength());
    }

    @Test
    void testGetNextIndex() {
        Assertions.assertEquals(data.length(), cacheLines.getNextIndex());
    }

    @Test
    void testGetPreviousIndex() {
        Assertions.assertEquals(0, cacheLines.getPreviousIndex());
    }

    @Test
    void testGetIndex() {
        Assertions.assertEquals(0, cacheLines.getIndex());
    }

    @Test
    void testIsModified() {
        Assertions.assertFalse(cacheLines.isModified());
        cacheLines.updateData(null);
        Assertions.assertTrue(cacheLines.isModified());
    }

    @Test
    void testGetPart() {
        Assertions.assertEquals(1, cacheLines.getPart());
    }

    @Test
    void testGetSize() {
        Assertions.assertEquals(12, cacheLines.getSize());
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        list.add(hex);

        cacheLines.updateData(list);
        Assertions.assertEquals(24, cacheLines.getSize());
    }
}