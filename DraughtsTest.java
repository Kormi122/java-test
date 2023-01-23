package tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import draughts.Piece;
import position.Direction;
import position.Position;

public class DraughtsTest {
	@Test
    public void directionTest() {
        int[] downLeftDist = new int[] {-10, -34};
        int[] downRightDist = new int[] {41, -55};
        int[] upLeftDist = new int[] {-13, 6};
        int[] upRightDist = new int[] {42, 9};

        String msg = "Direction.fromDirection nem jo iranyt ad vissza";
        assertAll(
        	() -> assertEquals(Direction.UP_LEFT, Piece.directionFromDistance(upLeftDist), msg),
        	() -> assertEquals(Direction.UP_RIGHT, Piece.directionFromDistance(upRightDist), msg),
        	() -> assertEquals(Direction.DOWN_LEFT, Piece.directionFromDistance(downLeftDist), msg),
        	() -> assertEquals(Direction.DOWN_RIGHT, Piece.directionFromDistance(downRightDist), msg)
        );
    }

    @Test
    void positionTest1() {
        Position p = new Position(52, 33);
        assertEquals(52, p.h, "A Position konstruktora nem jol inicializalja a vizszintes koordinatat");
        assertEquals(33, p.v, "A Position konstruktora nem jol inicializalja a fuggoleges koordinatat");
    }

    @Test
    void positionTest2() {
        Position p = new Position(52, 33);
        assertEquals("(52,33)", p.toString(), "A Position szoveges reprezentacioja nem jo alaku");
    }

    @Test
    void positionTest3() {
        String nextErrMsg = "A Position.next nem a jo iranyban levo szomszedos poziciot ad vissza - ";

        Position p = new Position(52, 33);

        assertAll(
            () -> {
                Position q1 = p.next(Direction.UP_LEFT);
                assertEquals(52 - 1, q1.h, nextErrMsg + Direction.UP_LEFT);
                assertEquals(33 + 1, q1.v, nextErrMsg + Direction.UP_LEFT);
            },
            () -> {
                Position q2 = p.next(Direction.UP_RIGHT);
                assertEquals(52 + 1, q2.h, nextErrMsg + Direction.UP_RIGHT);
                assertEquals(33 + 1, q2.v, nextErrMsg + Direction.UP_RIGHT);
            },
            () -> {
                Position q3 = p.next(Direction.DOWN_LEFT);
                assertEquals(52 - 1, q3.h, nextErrMsg + Direction.DOWN_LEFT);
                assertEquals(33 - 1, q3.v, nextErrMsg + Direction.DOWN_LEFT);
            },
            () -> {
                Position q4 = p.next(Direction.DOWN_RIGHT);
                assertEquals(52 + 1, q4.h, nextErrMsg + Direction.DOWN_RIGHT);
                assertEquals(33 - 1, q4.v, nextErrMsg + Direction.DOWN_RIGHT);
            }
        );
    }

    @Test
    void positionTest4() {
        Position p1a = new Position(94, 42);
        Position p1b = new Position(94, 42);
        Position p2 = new Position(2, 42);

        assertAll(
            () -> assertEquals(p1a, p1b, "Ket azonos pozicio nem egyenlo"),
            () -> assertNotEquals(p2, p1a, "Ket kulonbozo pozicio egyenlo"),
            () -> assertNotEquals("almafa", p1a, "Egy pozicio egyenlo egy szoveggel"),
            () -> assertNotEquals(null, p1a, "Egy pozicio egyenlo nullal")
        );
    }

    @Test
    void positionTest5() {
    	Position p = new Position(10, 16);
    	Position q = new Position(18, 9);
  		assertArrayEquals(new int[]{8, -7}, p.distance(q), "Position.distance nem jol szamolja a tavolsagot");
    }

    @Test
    void positionTest6() {
        String diagErr = "Position.isDiagonal szerint nem egy atlon helyezkednek el: ";
        String notDiagErr = "Position.isDiagonal szerint egy atlon helyezkednek el: ";

        Position p = new Position(1,1);

        assertTrue(p.isDiagonalTo(new Position(0,0)), diagErr + p + " es (0,0)");
        assertTrue(p.isDiagonalTo(new Position(2,2)), diagErr + p + " es (2,2)");
        assertTrue(p.isDiagonalTo(new Position(2,0)), diagErr + p + " es (2,0)");
        assertTrue(p.isDiagonalTo(new Position(0,2)), diagErr + p + " es (0,2)");
        assertFalse(p.isDiagonalTo(new Position(1,2)), notDiagErr + p + " es (1,2)");
        assertFalse(p.isDiagonalTo(new Position(2,1)), notDiagErr + p + " es (2,1)");
    }
}
