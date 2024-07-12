package hex.editor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionsTest {
    private final Positions positions = new Positions();

    @BeforeEach
    void setUp() {
        positions.removeAll();
    }

    @Test
    void testIsEmpty() {
        assertTrue(positions.isEmpty());
        positions.add(new Position(1, 2));
        assertFalse(positions.isEmpty());
    }

    @Test
    void testGetNext() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));

        positions.getNext();
        assertTrue(positions.getCurrent().getColumn() == 2 && positions.getCurrent().getRow() == 2);

        positions.getNext();
        assertTrue(positions.getCurrent().getColumn() == 2 && positions.getCurrent().getRow() == 2);
    }

    @Test
    void testGetPrevious() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));

        positions.getPrevious();
        assertTrue(positions.getCurrent().getColumn() == 1 && positions.getCurrent().getRow() == 1);

        positions.getPrevious();
        assertTrue(positions.getCurrent().getColumn() == 1 && positions.getCurrent().getRow() == 1);
    }

    @Test
    void testGetNextWithSize() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));

        positions.getNext(2);
        assertTrue(positions.getCurrent().getColumn() == 3 && positions.getCurrent().getRow() == 3);

        positions.getNext(2);
        assertTrue(positions.getCurrent().getColumn() == 3 && positions.getCurrent().getRow() == 3);
    }

    @Test
    void testGetPreviousWithSize() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));

        positions.getPrevious(2);
        assertTrue(positions.getCurrent().getColumn() == 1 && positions.getCurrent().getRow() == 1);
        positions.getNext(2);

        positions.getPrevious(1);
        assertTrue(positions.getCurrent().getColumn() == 2 && positions.getCurrent().getRow() == 2);
    }

    @Test
    void testGetCurrentWithSize() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));

        Position[] currentPositions = positions.getCurrent(2);
        assertEquals(2, currentPositions.length);
        assertTrue(currentPositions[0].getColumn() == 1 && currentPositions[0].getRow() == 1);
        assertTrue(currentPositions[1].getColumn() == 2 && currentPositions[1].getRow() == 2);
    }

    @Test
    void testAdd() {
        Position pos = new Position(1, 1);
        positions.add(pos);
        assertEquals(pos, positions.getCurrent());
    }

    @Test
    void testRemove() {
        Position pos = new Position(1, 1);
        positions.add(pos);
        positions.remove(pos);
        assertTrue(positions.isEmpty());
    }

    @Test
    void testRemoveAll() {
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));
        positions.removeAll();
        assertTrue(positions.isEmpty());
    }
}