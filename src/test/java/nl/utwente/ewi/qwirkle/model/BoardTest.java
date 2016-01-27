package nl.utwente.ewi.qwirkle.model;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;

    private Tile blueCircle;
    private Coordinate c1;

    private Tile redCross;
    private Coordinate c2;

    private Tile redCircle;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        blueCircle = new Tile(Tile.Shape.CIRCLE, Tile.Color.BLUE);
        c1 = new Coordinate(0, 0);

        redCross = new Tile(Tile.Shape.CROSS, Tile.Color.RED);
        c2 = new Coordinate(1, 0);

        redCircle = new Tile(Tile.Shape.CIRCLE, Tile.Color.RED);
    }

    @org.junit.Test
    public void testGet() throws Exception {
        board.put(c1, blueCircle);
        assertEquals(board.get(c1), blueCircle);
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        board.put(c1, blueCircle);
        board.remove(c1);
        assertEquals(board.get(c1), null);
    }

    @org.junit.Test
    public void testGetBoundaries() throws Exception {
        board.put(c1, blueCircle);
        board.put(c2, redCross);
        assertArrayEquals(board.getBoundaries(), new int[]{0, 1, 0, 0});
    }

    @org.junit.Test
    public void testIsEmpty() throws Exception {
        assertEquals(board.isEmpty(), true);
        board.put(c1, blueCircle);
        assertEquals(board.isEmpty(), false);
    }

    @org.junit.Test
    public void testGetScore() throws Exception {

    }

    @org.junit.Test
    public void testValidateMove() throws Exception {
        Map<Coordinate, Tile> move = new HashMap<>();
        move.put(c1, blueCircle);
        move.put(c2, redCircle);
        assertEquals(board.validateMove(move), true);
        move = new HashMap<>();
        move.put(c1, blueCircle);
        move.put(c2, redCross);
        assertEquals(board.validateMove(move), false);
    }
}