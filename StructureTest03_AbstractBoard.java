package tests.structure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static tests.util.CheckThat.Abstractness.ABSTRACT;
import static tests.util.CheckThat.Modifiability.NOT_MODIFIABLE;
import static tests.util.CheckThat.Staticness.NOT_STATIC;
import static tests.util.CheckThat.Visibility.PRIVATE;
import static tests.util.CheckThat.Visibility.PUBLIC;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest03_AbstractBoard {
	@Test
	public void structure() {
		assertAll(
			() ->
				CheckThat.theClass("board.AbstractBoard")
					.thatIs(ABSTRACT)
					.hasConstructorWithParams(int.class, int.class)
					.thatIs(PUBLIC)
			,() ->
				CheckThat.theClass("board.AbstractBoard")
					.hasFieldOfType("rows", int.class)
					.thatIs(PRIVATE, NOT_MODIFIABLE)
			,() ->
				CheckThat.theClass("board.AbstractBoard")
					.hasFieldOfType("cols", int.class)
					.thatIs(PRIVATE, NOT_MODIFIABLE)
			,() ->
				CheckThat.theClass("board.AbstractBoard")
					.hasMethodWithParams("contains", "position.Position")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(boolean.class)
			,() ->
				CheckThat.theClass("board.AbstractBoard")
					.hasMethodWithParams("get", "position.Position")
					.thatIs(PUBLIC)
					.thatThrows(IndexOutOfBoundsException.class)
			,() ->
				CheckThat.theClass("board.AbstractBoard")
					.hasMethodWithParams("set", "position.Position", "java.lang.Object")
					.thatIs(PUBLIC)
					.thatThrows(IndexOutOfBoundsException.class)
					.thatReturnsNothing()
		);
	}
}
