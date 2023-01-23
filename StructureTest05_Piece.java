package tests.structure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static tests.util.CheckThat.Abstractness.NOT_ABSTRACT;
import static tests.util.CheckThat.Staticness.NOT_STATIC;
import static tests.util.CheckThat.Staticness.STATIC;
import static tests.util.CheckThat.Visibility.PUBLIC;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest05_Piece {
	@Test
	public void structure() {
		assertAll(
			() ->
				CheckThat.theEnum("draughts.Color")
					.thatIs(PUBLIC, NOT_STATIC)
					.hasEnumElements("WHITE", "BLACK")
			,() ->
				CheckThat.theClass("draughts.Piece")
					.thatIs(PUBLIC, NOT_ABSTRACT)
					.hasConstructorWithParams("position.Position", "draughts.Color", "board.Board")
					.thatIs(PUBLIC)
					,() ->
					CheckThat.theClass("draughts.Piece")
						.hasMethodWithParams("canMoveTo", "position.Position")
						.thatIs(PUBLIC, NOT_STATIC)
						.thatReturns(boolean.class)
			,() ->
				CheckThat.theClass("draughts.Piece")
					.hasMethodWithParams("directionFromDistance", int[].class)
					.thatIs(PUBLIC, STATIC)
					.thatReturns("position.Direction")
			,() ->
				CheckThat.theClass("draughts.Piece")
					.hasMethodWithParams("moveTo", "position.Position")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturnsNothing()
			,() ->
				CheckThat.theClass("draughts.Piece")
					.hasMethodWithNoParams("toString")
					.thatIs(PUBLIC, NOT_STATIC, NOT_ABSTRACT)
					.thatReturns(String.class)

			,() ->
				CheckThat.theCheckedException("draughts.InvalidMoveException")
					.hasConstructorWithParams("position.Position", "position.Position")
					.thatIs(PUBLIC)
		);
	}
}
