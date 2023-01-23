package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import board.Board;
import board.SparseBoard;
import draughts.Color;
import draughts.King;
import draughts.Piece;
import position.Direction;
import position.Position;

public class KingTest {
    @Test
    public void kingTest1() {
    	int h = 5;
    	int w = 5;
        Board<Piece> grid = new SparseBoard<Piece>(h, w);

        Position center = new Position(2,2);
        Position centerClone = new Position(2,2);
        Position notDiag = new Position(2,1);
        Position oneStep = new Position(1,3);
        Position twoStep = new Position(0,4);

        King m = new King(center, Color.WHITE, grid);

        String regularErr = "A King.canMoveTo hamisat adott egy szabalyos lepes eseten: ";
        assertTrue(m.canMoveTo(oneStep), regularErr + center + " -> " + oneStep);

        String irregularErr = "A King.canMoveTo igazat adott egy szabalytalan lepes eseten: ";
        assertFalse(m.canMoveTo(twoStep), irregularErr + "ket lepes tavolsag " + center + " -> " + twoStep);
        assertFalse(m.canMoveTo(notDiag), irregularErr + "nem atlos lepes " + center + " -> " + notDiag);
        assertFalse(m.canMoveTo(centerClone), irregularErr + "nem is akarunk lepni " + center + " -> " + centerClone);


        Position side = new Position(0,2);
        Position offBoard = side.next(Direction.UP_LEFT);
        King m0 = new King(side, Color.WHITE, grid);
        assertFalse(m0.canMoveTo(offBoard), irregularErr + side + " -> " + offBoard);
    }

    @Test
    public void kingTest2() {
    	int h = 5;
    	int w = 5;
        Board<Piece> grid = new SparseBoard<Piece>(h, w);

    	Position p1 = new Position(2,2);
        Position p2 = new Position(1,1);
        Position p3 = new Position(3,1);
        Position p4 = new Position(3,3);
        Position p5 = new Position(4,4);
        King m1 = new King(p1, Color.BLACK, grid);
        King enemy = new King(p2, Color.WHITE, grid);
        King m2 = new King(p3, Color.BLACK, grid);
        King enemy2 = new King(p4, Color.WHITE, grid);
        King m3 = new King(p5, Color.BLACK, grid);
        grid.set(p1, m1);
        grid.set(p2, enemy);
        grid.set(p3, m2);
        grid.set(p4, enemy2);
        grid.set(p5, m3);

        Position jump = new Position(0,0);
        Position noJump = new Position(4,0);
        Position otherDirection = new Position(1,3);
        Position notEmpty = new Position(3,1);

        assertTrue(m1.canMoveTo(jump), "A King.canMoveTo hamisat adott egy szabalyos utes eseten");
        assertFalse(m1.canMoveTo(noJump), "A King.canMoveTo igazat adott egy szabalytalan utes eseten: sajat babu atugrasa");
        assertFalse(m1.canMoveTo(notEmpty), "A King.canMoveTo igazat adott, ha egy nem ures mezore lepnenk");
        assertTrue(m1.canMoveTo(otherDirection), "A King.canMoveTo hamisat adott szabalyos lepes eseten");
        assertFalse(m3.canMoveTo(p1), "A King.canMoveTo igazat adott, ha utnenk, de a cel mezo nem ures");

        assertEquals("BK", m1.toString(), "A King.toString nem jo szoveget adott vissza");
    }
}
