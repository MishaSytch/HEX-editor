package hex.editor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

    @Test
    void testGetRow() {
        Position position = new Position(2, 3);
        assertEquals(2, position.getRow());
    }

    @Test
    void testGetColumn() {
        Position position = new Position(2, 3);
        assertEquals(3, position.getColumn());
    }

    @Test
    void testConstructor() {
        Position position = new Position(2, 3);
        assertEquals(2, position.getRow());
        assertEquals(3, position.getColumn());
    }
}