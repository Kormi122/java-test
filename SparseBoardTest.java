package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import board.Board;
import board.SparseBoard;
import position.Position;

public class SparseBoardTest {
    private Board<Integer> grid;

    @BeforeEach
    public void beforeEach() {
        grid = new SparseBoard<>(2,3);
    }

	@ParameterizedTest
	@CsvSource({"true,2,1", "false,1,2", "false,3,1", "false,0,-1", "false,-1,0"})
    public void gridTest1(boolean isOK, int x, int y) {
        assertEquals(isOK, grid.contains(new Position(x, y)),
        	String.format("Az isValid %s erteket adott %s pozicio eseten: (%d, %d)",
        		isOK ? "hamis" : "igaz",
				isOK ? "helyes" : "ervenytelen",
        		x, y));
	}

	@Test
    public void gridTest2() {
        Position valid = new Position(2,1);
		assertNull(grid.get(valid), "A get nem null-t ad vissza kezdetben");
	}

	@Test
    public void gridTest3() {
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> {
            Position invalid = new Position(1,2);
        	grid.get(invalid);
        }, "A get nem null-t ad vissza kezdetben");

        assertEquals("Invalid position: (1,2)", ex.getMessage());
	}

	@Test
	public void gridTest4() {
		IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> {
			Position invalid = new Position(1,2);
			grid.set(invalid, 100);
		}, "A set nem dobott IndexOutOfBoundsException kivetelt ervenytelen index eseten");

		assertEquals("Invalid position: (1,2)", ex.getMessage());
	}

	@Test
	public void gridTest5() {
        Position valid = new Position(2,1);
        grid.set(valid, 25);
        assertEquals(25, grid.get(valid), "A set nem jo poziciora teszi az uj elemet, vagy a get nem jo helyrol adja vissza");
    }
}
