package tests.structure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static tests.util.CheckThat.Abstractness.NOT_ABSTRACT;
import static tests.util.CheckThat.Visibility.PRIVATE;
import static tests.util.CheckThat.Visibility.PUBLIC;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest04_SparseBoard {
	@Test
	public void structure() {
		assertAll(
			() ->
				CheckThat.theClassWithParent("board.SparseBoard", "board.AbstractBoard")
					.thatIs(NOT_ABSTRACT)
					.hasConstructorWithParams(int.class, int.class)
					.thatIs(PUBLIC)
			,() ->
				CheckThat.theClass("board.SparseBoard")
					.hasFieldOfType("board", "java.util.Map")
					.thatIs(PRIVATE)
			,() ->
				CheckThat.theClass("board.SparseBoard")
					.hasMethodWithParams("get", "position.Position")
					.thatIs(PUBLIC)
					.thatThrows(IndexOutOfBoundsException.class)
			,() ->
				CheckThat.theClass("board.SparseBoard")
					.hasMethodWithParams("set", "position.Position", "java.lang.Object")
					.thatIs(PUBLIC)
					.thatThrows(IndexOutOfBoundsException.class)
					.thatReturnsNothing()
		);
	}
}
