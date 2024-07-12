package hex.editor.view.Panel.origin;

import hex.editor.model.CacheLines;
import hex.editor.view.MainWindow;
import hex.editor.view.Panel.InfoPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPanelTest {
    WorkPanel workPanel;
    CacheLines cacheLines;

    @BeforeEach
    void setUp() {
        workPanel = new WorkPanel(new MainWindow(), new InfoPanel(new MainWindow()));
        String data = "48 65 6C 6C 6f 20 57 6f 72 6C 64 21 ";
        List<String> hex = new ArrayList<>(Arrays.asList(data.toLowerCase().trim().split(" ")));
        List<List<String>> list = new ArrayList<>();
        list.add(hex);
        cacheLines = new CacheLines(list, 1);
        workPanel.setHex(cacheLines);
    }

    @Test
    public void testGetHex() {

        List<List<String>> hex = workPanel.getHex();

        assertNotNull(hex);
        assertEquals(cacheLines.getData(), hex);
    }

    @Test
    public void testSetHex() {
        assertNotNull(workPanel.getHex());
        assertEquals(cacheLines.getData(), workPanel.getHex());
    }

    @Test
    public void testSearchByMask() {
        assertDoesNotThrow(() -> workPanel.searchByMask("test mask"));

    }

    @Test
    public void testSearchByHex() {
        assertDoesNotThrow(() -> workPanel.searchByHex(Arrays.asList("hex1", "hex2", "hex3")));
    }

    @Test
    public void testNextPosition() {
        WorkPanel workPanel = new WorkPanel(new MainWindow(), new InfoPanel(new MainWindow()));

        assertDoesNotThrow(workPanel::nextPosition);
    }
}