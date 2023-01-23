package tests.structure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static tests.util.CheckThat.Abstractness.NOT_ABSTRACT;
import static tests.util.CheckThat.Staticness.NOT_STATIC;
import static tests.util.CheckThat.Visibility.PUBLIC;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest06_King {
	@Test
	public void structure() {
		assertAll(
			() ->
				CheckThat.theClassWithParent("draughts.King", "draughts.Piece")
					.thatIs(PUBLIC, NOT_ABSTRACT)
					.hasConstructorWithParams("position.Position", "draughts.Color", "board.Board")
					.thatIs(PUBLIC)
			,() ->
				CheckThat.theClass("draughts.King")
					.hasMethodWithParams("canMoveTo", "position.Position")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(boolean.class)
			,() ->
				CheckThat.theClass("draughts.King")
					.hasMethodWithNoParams("toString")
					.thatIs(PUBLIC, NOT_STATIC, NOT_ABSTRACT)
					.thatReturns(String.class)
		);
	}
}
