package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import board.Board;
import board.SparseBoard;
import draughts.Color;
import draughts.InvalidMoveException;
import draughts.Piece;
import position.Direction;
import position.Position;

public class PieceTest {
    @ParameterizedTest
    @CsvSource({"UP_RIGHT,1,1", "UP_LEFT,-1,1", "DOWN_RIGHT,1,-1", "DOWN_LEFT,-1,-1"})
    public void directionFromDistance(Direction expectedDir, int xDistance, int yDistance) {
    	assertEquals(expectedDir, Piece.directionFromDistance(new int[] {xDistance, yDistance}));
    }

    @Test
    public void test1() {
    	int h = 5;
    	int w = 5;
        Board<Piece> grid = new SparseBoard<Piece>(h, w);

        Position center = new Position(2,2);
        Position centerClone = new Position(2,2);
        Position notDiag = new Position(2,1);
        Position oneStep = new Position(1,1);
        Position oneOneStep = new Position(0,0);
        Position twoStep = new Position(4,0);

        Piece m = new Piece(center, Color.BLACK, grid);

        String regularErr = "A Piece.canMoveTo hamisat adott egy szabalyos lepes eseten: ";
        assertTrue(m.canMoveTo(oneStep), regularErr + center + " -> " + oneStep);

        String irregularErr = "A Piece.canMoveTo igazat adott egy szabalytalan lepes eseten: ";
        assertFalse(m.canMoveTo(twoStep), irregularErr + "ket lepes tavolsag " + center + " -> " + twoStep);
        assertFalse(m.canMoveTo(notDiag), irregularErr + "nem atlos lepes " + center + " -> " + notDiag);
        assertFalse(m.canMoveTo(centerClone), irregularErr + "nem is akarunk lepni " + center + " -> " + centerClone);

        assertDoesNotThrow(() -> {
            m.moveTo(oneStep);
        }, "A Piece.stepTo valamilyen kivetelt dobott szabalyos lepes eseten");

        assertTrue(m.canMoveTo(oneOneStep), regularErr + "meg egyet lepunk " + oneStep + " -> " + oneOneStep);

        Position side = new Position(0,2);
        Position offBoard = side.next(Direction.UP_LEFT);
        Piece m0 = new Piece(side, Color.BLACK, grid);
        assertFalse(m0.canMoveTo(offBoard), regularErr + side + " -> " + offBoard);
    }

    @Test
    public void test2() {
    	int h = 5;
    	int w = 5;
        Board<Piece> grid = new SparseBoard<Piece>(h, w);

        Position p1 = new Position(2,2);
        Position p2 = new Position(1,1);
        Position p3 = new Position(3,1);
        Position p4 = new Position(3,3);
        Position p5 = new Position(4,4);
        Piece m1 = new Piece(p1, Color.BLACK, grid);
        Piece enemy = new Piece(p2, Color.WHITE, grid);
        Piece m2 = new Piece(p3, Color.BLACK, grid);
        Piece enemy2 = new Piece(p4, Color.WHITE, grid);
        Piece m3 = new Piece(p5, Color.BLACK, grid);
        grid.set(p1, m1);
        grid.set(p2, enemy);
        grid.set(p3, m2);
        grid.set(p4, enemy2);
        grid.set(p5, m3);

        Position jump = new Position(0,0);
        Position noJump = new Position(4,0);
        Position wrongDirection = new Position(1,3);
        Position notEmpty = new Position(3,1);

        assertTrue(m1.canMoveTo(jump), "A Piece.canMoveTo hamisat adott egy szabalyos utes eseten");
        assertFalse(m1.canMoveTo(noJump), "A Piece.canMoveTo igazat adott egy szabalytalan utes eseten: sajat babu atugrasa");
        assertFalse(m1.canMoveTo(notEmpty), "A Piece.canMoveTo igazat adott, ha egy nem ures mezore lepnenk");
        assertFalse(m1.canMoveTo(wrongDirection), "A Piece.canMoveTo igazat adott, ha rossz iranyba lepnenk");
        assertFalse(m3.canMoveTo(p1), "A Piece.canMoveTo igazat adott, ha utnenk, de a cel mezo nem ures");

        assertThrows(InvalidMoveException.class, () -> {
            m1.moveTo(wrongDirection);
        }, "A Piece.moveTo nem dobott InvalidMoveException kivetelt szabalytalan utes eseten");

        assertDoesNotThrow(() -> {
            m1.moveTo(jump);
        }, "A Piece.moveTo InvalidMoveException kivetelt dobott szabalyos utes eseten");
        assertNull(grid.get(p2), "A Piece.moveTo nem vette le az ellenfel leutott babujat a tablarol");
        assertNull(grid.get(p1), "A Piece.moveTo nem tette uresse a babu eredeti helyet a tablan");
        assertTrue(grid.get(jump) == m1, "A Piece.moveTo nem tette at a babut az új helyre a tablan");

        assertEquals("BM", m1.toString(), "A Piece szoveges reprezentacioja nem jo");
    }
}
